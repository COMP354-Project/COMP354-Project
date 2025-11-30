package database.exceptions;
/**
 * Thrown to indicate that a user being added to the database
 * already exists.
 */
public class UserAlreadyExistedException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining why the user already exists
     */
    public UserAlreadyExistedException(String message) {
        super(message);
    }
}
