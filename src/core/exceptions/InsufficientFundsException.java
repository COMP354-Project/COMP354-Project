package core.exceptions;

public class InsufficientFundsException extends RuntimeException{
    private final String ERROR_MESSAGE = "Error! Insufficient funds!";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
