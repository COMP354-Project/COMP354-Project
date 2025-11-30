package database.exceptions;
/**
 * Thrown to indicate that a requested branch could not be found in the database.
 */
public class BranchNotFoundException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining why the exception was thrown
     */
    public BranchNotFoundException(String message) {
        super(message);
    }
}
