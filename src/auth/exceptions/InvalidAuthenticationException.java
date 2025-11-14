package auth.exceptions;


/**
 * Thrown to indicate that the provided email and password combination
 * does not match any existing user record in the database.
 */
public class InvalidAuthenticationException extends RuntimeException {
    private final String ERROR_MESSAGE = "Error! Wrong email or password.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
