package auth.exceptions;

public class InsufficientFundsException extends RuntimeException{
    private final String ERROR_MESSAGE = "Error! Wrong email or password.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
