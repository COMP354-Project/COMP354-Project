package database;
/**
 * DBDriver serves as an entry point for testing the {@link DatabaseSingleton} operations.
 *
 * <p>This class is intended for manual testing of database operations during development.
 * It initializes the singleton database instance and can be used to add, fetch, or update
 * users, accounts, and transactions.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     DatabaseSingleton db = DatabaseSingleton.getDatabase();
 *     // Perform database operations here
 * </pre>
 *
 * Note: This class is not part of the production logic; it is meant for development and testing purposes.
 */
public class DBDriver {
    public static void main(String[] args) {
        DatabaseSingleton db = DatabaseSingleton.getDatabase();
        // Test your database operations here
    }
}
