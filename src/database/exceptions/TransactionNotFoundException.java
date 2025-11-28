package database.exceptions;
/**
 * Thrown to indicate that a transaction with the specified ID
 * could not be found in the database.
 */
public class TransactionNotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining why the transaction was not found
     */
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
