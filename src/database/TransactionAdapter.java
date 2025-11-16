package database;

import bank.Transaction;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class TransactionAdapter extends TypeAdapter<Transaction> {

    @Override
    public void write(JsonWriter writer, Transaction val) throws IOException {

        if (val == null) {
            writer.nullValue();
        } else {
            writer.value(val.getId());
        }
    }

    @Override
    public Transaction read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
