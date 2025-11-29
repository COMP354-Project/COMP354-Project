package core.exceptions;


/**
 * Thrown to indicate that a password does not comply with the required format restrictions.
 */
public class InvalidInputException extends Exception {
    /**
     * Default message indicating the input format is incorrect.
     */
    private final String ERROR_MESSAGE = "Wrong input format";

    /**
     * Returns the error message associated with this exception.
     *
     * @return a string indicating that the input format is incorrect
     */
    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
