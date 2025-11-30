package auth.exceptions;

/**
 * Thrown to indicate that an account already exists in the system.
 * <p>
 * This is a runtime exception and typically used when trying to create or register
 * a duplicate account.
 * </p>
 */
public class DuplicateAccountException extends RuntimeException {
    /** Default error message for this exception. */
    private final String ERROR_MESSAGE = "Duplicate!";
    /**
     * Returns the error message associated with this exception.
     *
     * @return the error message "Duplicate!"
     */
    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
