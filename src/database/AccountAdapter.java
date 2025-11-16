package database;

import bank.Account;
import com.google.gson.TypeAdapter;

import java.io.IOException;

public class AccountAdapter extends  TypeAdapter<Account> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, Account value) throws IOException {
        // Implementation for serializing Account object to JSON
        if (value == null) {
            out.nullValue();
        }
        else {
            out.value(value.getAccountID());
        }
    }

    @Override
    public Account read(com.google.gson.stream.JsonReader in) {
        // Implementation for deserializing JSON to Account object
        return null;
    }
}
