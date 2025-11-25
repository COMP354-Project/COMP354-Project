package database;

import database.DatabaseSingleton;

public class DBDriver {
    public static void main(String[] args) {
        DatabaseSingleton dbInstance = DatabaseSingleton.getDatabase();
        dbInstance.printAccounts();
        dbInstance.printTransactions();
    }
}
