package auth.exceptions;


/**
 * Thrown to indicate that a password does not comply with the required format restrictions.
 */
public class InvalidInputException extends Exception {
    private final String ERROR_MESSAGE = "Wrong input format";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
