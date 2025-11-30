package core.exceptions;
/**
 * Thrown to indicate that a requested transaction or operation
 * cannot be completed because the account does not have sufficient funds.
 */
public class InsufficientFundsException extends RuntimeException{
    /**
     * Error message displayed when a transaction fails due to insufficient funds.
     */
    private final String ERROR_MESSAGE = "Error! Insufficient funds!";
    /**
     * Returns a detailed message for this exception.
     *
     * @return a string describing the insufficient funds error
     */
    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
