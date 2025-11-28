package database.exceptions;
/**
 * Thrown to indicate that the provided username is invalid or does not exist in the database.
 */
public class InvalidUsernameException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining why the username is invalid
     */
    public InvalidUsernameException(String message) {
        super(message);
    }
}
