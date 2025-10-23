package database;

/**
 * An singleton injection of the database instance
 */
public class DatabaseSingleton {
    public static DatabaseSingleton db;

    public DatabaseSingleton getDataBase() {
        // If database isn't instantiated, initialise it and return it
        // Else, return the initialised database
        if (db == null) {
            db = new DatabaseSingleton();
        }
        return db;
    }



}
