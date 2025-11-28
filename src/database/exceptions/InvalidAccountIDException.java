package database.exceptions;
/**
 * Thrown to indicate that an account ID provided is invalid or does not exist in the database.
 */
public class InvalidAccountIDException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public InvalidAccountIDException(String message) {
        super(message);
    }
}
