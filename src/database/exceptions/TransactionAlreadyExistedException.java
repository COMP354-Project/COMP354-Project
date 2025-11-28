package database.exceptions;
/**
 * Thrown to indicate that a transaction already exists in the database
 * and cannot be added again.
 */
public class TransactionAlreadyExistedException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining why the transaction already exists
     */
    public TransactionAlreadyExistedException(String message) {
        super(message);
    }
}
