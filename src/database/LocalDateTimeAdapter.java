package database;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(JsonWriter writer, LocalDateTime val) throws IOException {
        if (val == null) {
            writer.nullValue();
        } else {
            writer.value(val.toString());
        }
    }

    @Override
    public LocalDateTime read(JsonReader reader) throws IOException {
        if (reader.peek() == com.google.gson.stream.JsonToken.NULL) {
            reader.nextNull();
            return null;
        } else {
            String dateTimeString = reader.nextString();
            return LocalDateTime.parse(dateTimeString);
        }
    }
}
