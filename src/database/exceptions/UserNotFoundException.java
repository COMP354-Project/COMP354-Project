package database.exceptions;
/**
 * Thrown to indicate that a requested user was not found
 * in the database.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining why the user was not found
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
