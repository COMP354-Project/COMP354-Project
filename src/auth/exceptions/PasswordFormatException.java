package auth.exceptions;


/**
 * Thrown to indicate that a password does not comply with the required format restrictions.
 */
public class PasswordFormatException extends RuntimeException {
    private final String ERROR_MESSAGE = "Wrong password format";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
