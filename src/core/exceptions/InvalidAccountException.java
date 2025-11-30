package core.exceptions;
/**
 * Thrown to indicate that the specified account could not be found
 * or is invalid for the requested operation.
 */
public class InvalidAccountException extends Exception{
    /** Error message returned when the specified account cannot be located. */
    private final String ERROR_MESSAGE = "Error! Account not found.";
    /**
     * Returns the error message for this exception.
     *
     * @return a string indicating the account is invalid or not found
     */
    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
