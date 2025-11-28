package auth.exceptions;
/**
 * Thrown to indicate that a system operation has timed out
 * before completion.
 */
public class TimeOutException extends RuntimeException{
    /** Default error message when a timeout occurs. */
    private final String ERROR_MESSAGE = "System timed out.";
    /**
     * Returns the error message for this exception.
     *
     * @return the default timeout error message
     */
    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }

    // TODO: Implement attributes and message display (toString) for this exception.
}
