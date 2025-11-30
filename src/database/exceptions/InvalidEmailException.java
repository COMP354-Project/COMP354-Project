package database.exceptions;
/**
 * Thrown to indicate that the provided email is invalid or does not exist in the database.
 */
public class InvalidEmailException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining why the email is invalid
     */
    public InvalidEmailException(String message) {
        super(message);
    }
}
