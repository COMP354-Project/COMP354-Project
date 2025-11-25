package database;

import auth.core.*;
import com.google.gson.*;
import core.exceptions.InvalidInputException;

import java.lang.reflect.Type;

public class UserAdapter  implements JsonSerializer<User>, JsonDeserializer<User> {
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();

        obj.addProperty("email", user.getEmail());
        obj.addProperty("password", user.getPassword());

        // Add the type field (concrete class name)
        if (user instanceof Customer) {
            obj.addProperty("firstName", ((Customer) user).getFirstName());
            obj.addProperty("lastName", ((Customer) user).getLastName());
            obj.addProperty("type", "Customer");
        } else if (user instanceof Teller) {
            obj.addProperty("type", "Teller");
        } else if (user instanceof Admin) {
            obj.addProperty("type", "Admin");
        }
        return obj;
    }
    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        User user = null;
        JsonObject obj = json.getAsJsonObject();
        String type = obj.has("type") ? obj.get("type").getAsString() : null;
        // Instantiate appropriate subclass (Chequing, Savings, ...) based on `type`.
        // then populate fields (customer, balance, accountStatus, transactions -> ids).
        switch (type) {
            case "Customer":
                user = new Customer(obj.get("email").getAsString(),
                        obj.get("password").getAsString(),
                        obj.get("firstName").getAsString(),
                        obj.get("lastName").getAsString());
                break;
            case "Teller":
                user = new Teller();
                user.setEmail(obj.get("email").getAsString());
                try {
                    user.setPassword(obj.get("password").getAsString());
                }
                catch (InvalidInputException e) {
                    System.out.println("Invalid password format for Teller user.");
                }
                break;
            case "Admin":
                user = new Admin();
                user.setEmail(obj.get("email").getAsString());
                try {
                    user.setPassword(obj.get("password").getAsString());
                }
                catch (InvalidInputException e) {
                    System.out.println("Invalid password format for Admin user.");
                }
                break;
            default:
                // Handle unknown type or base Account if needed
                break;
        }
        return user;
    }

}
