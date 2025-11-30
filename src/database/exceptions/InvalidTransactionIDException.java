package database.exceptions;
/**
 * Thrown to indicate that the provided transaction ID is invalid or does not exist in the database.
 */
public class InvalidTransactionIDException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining why the transaction ID is invalid
     */
    public InvalidTransactionIDException(String message) {
        super(message);
    }
}
