package database.exceptions;
/**
 * Thrown to indicate that a requested account could not be found in the database.
 */
public class AccountNotFoundException extends RuntimeException{
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public AccountNotFoundException(String message) {
        super(message);
    }
}
