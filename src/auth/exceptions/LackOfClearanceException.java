package auth.exceptions;

/**
 * Thrown to indicate that a user does not have sufficient clearance
 * to perform a certain action in the system.
 * <p>
 * This is a specialized type of {@link InvalidAuthenticationException}.
 * </p>
 */
public class LackOfClearanceException extends InvalidAuthenticationException {
    /** Default error message when permission is denied. */
    private final String DEFAULT_ERROR_MESSAGE = "Permission not granted!";

    /**
     * Returns the error message indicating that the user lacks clearance.
     *
     * @return the error message
     */
    @Override
    public String getMessage() {
        return DEFAULT_ERROR_MESSAGE;
    }
}
