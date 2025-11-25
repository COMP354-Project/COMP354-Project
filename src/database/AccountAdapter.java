package database;

import auth.core.Customer;
import auth.core.User;
import bank.*;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * <p>Custom Gson adapter for serializing and deserializing Account objects,
 * including their specific subclasses (Chequing, Saving, Card). </p>
 *
 * <p>During serialization, it adds a "type" field to indicate the concrete class.</p>
 * <p>During deserialization, it reads the "type" field to instantiate the correct subclass.</p>
 *
 * <p>Account's <em>transaction</em> field is ignored during serialization to avoid circular referencing with Transaction, which leads to infinite recursion.
 * However, the list of transactions will be loaded when the Transaction objects are loaded to memory.</p>
 *
 * @see Account
 * @see Chequing
 * @see Saving
 * @see Card
 *
 * @author Cong Minh Le
 */
public class AccountAdapter implements JsonSerializer<Account>, JsonDeserializer<Account> {

    @Override
    public JsonElement serialize(Account account, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("accountId", account.getAccountID());
        obj.addProperty("customer", account.getCustomer().getEmail());
        obj.addProperty("accountStatus", account.getAccountStatus().toString());

        // Add the type field (concrete class name)
        if (account instanceof Chequing) {
            obj.addProperty("type", "Chequing");
        } else if (account instanceof Saving) {
            obj.addProperty("type", "Saving");
        } else if (account instanceof Card) {
            obj.addProperty("creditLimit", ((Card) account).getCreditLimit());
            obj.addProperty("creditUsage", ((Card) account).getCreditUsage());
            obj.addProperty("type", "Card");
        }
        else{
            obj.addProperty("type", "Account");
        }

        return obj;
    }

    @Override
    public Account deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Account acc = null;
        JsonObject obj = json.getAsJsonObject();
        String type = obj.has("type") ? obj.get("type").getAsString() : null;
        User user = UserData.getUserData().getUserByEmail(obj.get("customer").getAsString());
        switch (type) {
            case "Chequing":
                acc = new Chequing((Customer) user);
                acc.setAccountId(obj.get("accountId").getAsString());
                acc.setActivity(Account.AccountStatus.valueOf(obj.get("accountStatus").getAsString()));
                break;
            case "Saving":
                acc = new Saving((Customer) user);
                acc.setAccountId(obj.get("accountId").getAsString());
                acc.setActivity(Account.AccountStatus.valueOf(obj.get("accountStatus").getAsString()));
                break;
            case "Card":
                acc = new Card((Customer) user, 0.0);
                acc.setAccountId(obj.get("accountId").getAsString());
                acc.setActivity(Account.AccountStatus.valueOf(obj.get("accountStatus").getAsString()));
                ((Card) acc).setCreditLimit(obj.get("creditLimit").getAsDouble());
                ((Card) acc).setCreditUsage(obj.get("creditUsage").getAsDouble());
                break;
            default:
                // Handle unknown type or base Account if needed
                break;
        }
        return acc;
    }
}