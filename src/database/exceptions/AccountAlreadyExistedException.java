package database.exceptions;
/**
 * Thrown to indicate that an attempt was made to create an account
 * that already exists in the database.
 */
public class AccountAlreadyExistedException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public AccountAlreadyExistedException(String message) {
        super(message);
    }
}
