package database;

import auth.core.*;
import com.google.gson.*;
import core.exceptions.InvalidInputException;

import java.lang.reflect.Type;

/**
 * Custom Gson adapter for serializing and deserializing {@link User} objects,
 * including their specific subclasses ({@link Customer}, {@link Teller}, {@link Admin}).
 *
 * <p>During serialization, it adds a "type" field to indicate the concrete subclass of {@link User}.</p>
 *
 * <p>During deserialization, it reads the "type" field to instantiate the correct subclass and
 * populate its fields (email, password, and subclass-specific attributes).</p>
 *
 * <p>Example usage with Gson:</p>
 * <pre>
 *     Gson gson = new GsonBuilder()
 *                   .registerTypeAdapter(User.class, new UserAdapter())
 *                   .create();
 * </pre>
 *
 * @see User
 * @see Customer
 * @see Teller
 * @see Admin
 * @author Cong Minh Le
 */
public class UserAdapter implements JsonSerializer<User>, JsonDeserializer<User> {
    /**
     * Serializes a {@link User} object into a JSON element.
     *
     * @param user the user object to serialize
     * @param type the actual type (User or subclass)
     * @param jsonSerializationContext the Gson serialization context
     * @return the JSON representation of the user
     */
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
            obj.addProperty("branchID", ((Teller) user).getBranchID());
            obj.addProperty("type", "Teller");
        } else if (user instanceof Admin) {
            obj.addProperty("type", "Admin");
        }
        return obj;
    }
    /**
     * Deserializes a {@link User} object from a JSON element.
     *
     * @param json the JSON element containing the user data
     * @param typeOfT the type to deserialize into
     * @param context the Gson deserialization context
     * @return the deserialized {@link User} object
     * @throws JsonParseException if the JSON cannot be parsed
     */
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
                user.setPassword(obj.get("password").getAsString());
                // is there a better way to do this?
                ((Teller) user).setBranchID(obj.get("branchID").getAsString());
                break;
            case "Admin":
                user = new Admin();
                user.setEmail(obj.get("email").getAsString());
                user.setPassword(obj.get("password").getAsString());
                break;
            default:
                // Handle unknown type or base Account if needed
                break;
        }
        return user;
    }

}
