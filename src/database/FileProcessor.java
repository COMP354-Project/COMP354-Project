package database;

import com.google.gson.Gson;

public interface FileProcessor {
    void load();
    void save();
    Gson getGson();
}
