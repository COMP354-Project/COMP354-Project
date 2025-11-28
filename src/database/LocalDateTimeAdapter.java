package database;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
/**
 * Custom Gson {@link TypeAdapter} for serializing and deserializing {@link LocalDateTime} objects.
 *
 * <p>This adapter converts a {@link LocalDateTime} to a JSON string using
 * {@link LocalDateTime#toString()} during serialization, and parses the string
 * back to a {@link LocalDateTime} during deserialization.</p>
 *
 * <p>Null values are supported: they are written as JSON null and read back as {@code null}.</p>
 *
 * <p>Example usage with Gson:</p>
 * <pre>
 *     Gson gson = new GsonBuilder()
 *                   .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
 *                   .create();
 * </pre>
 *
 * @see LocalDateTime
 * @see com.google.gson.TypeAdapter
 */
public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    /**
     * Serializes a {@link LocalDateTime} to JSON.
     *
     * @param writer the {@link JsonWriter} to write to
     * @param val the {@link LocalDateTime} value to serialize, or null
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void write(JsonWriter writer, LocalDateTime val) throws IOException {
        if (val == null) {
            writer.nullValue();
        } else {
            writer.value(val.toString());
        }
    }
    /**
     * Deserializes a {@link LocalDateTime} from JSON.
     *
     * @param reader the {@link JsonReader} to read from
     * @return the deserialized {@link LocalDateTime}, or null if JSON value is null
     * @throws IOException if an I/O error occurs
     */
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
