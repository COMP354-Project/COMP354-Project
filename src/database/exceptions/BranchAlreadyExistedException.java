package database.exceptions;
/**
 * Thrown to indicate that a branch being added already exists in the database.
 */
public class BranchAlreadyExistedException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining why the exception was thrown
     */
    public BranchAlreadyExistedException(String message) {
        super(message);
    }
}
