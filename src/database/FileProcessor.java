package database;

import com.google.gson.Gson;
/**
 * Interface representing a generic file processor for loading and saving data.
 *
 * <p>Implementations of this interface should define how data is loaded from and
 * saved to a storage medium (e.g., JSON files) and provide a {@link Gson} instance
 * for serialization and deserialization of objects.</p>
 *
 * <p>Typical usage:</p>
 * <pre>
 *     FileProcessor processor = new JsonFileProcessor();
 *     processor.load();
 *     processor.save();
 *     Gson gson = processor.getGson();
 * </pre>
 */
public interface FileProcessor {
    /**
     * Loads data from the storage medium into memory.
     * Implementations should handle reading the data source and populating
     * relevant objects.
     */
    void load();
    /**
     * Saves data from memory to the storage medium.
     * Implementations should handle writing objects into the persistent storage.
     */
    void save();
    /**
     * Returns the {@link Gson} instance used for serialization/deserialization.
     *
     * @return Gson instance for JSON processing
     */
    Gson getGson();
}
