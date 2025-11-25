package database;

import auth.core.*;
import database.DatabaseSingleton;

public class DBDriver {
    public static void main(String[] args) {
        DatabaseSingleton db = DatabaseSingleton.getDatabase();
    }
}
