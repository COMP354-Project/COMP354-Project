package core.exceptions;

public class InvalidAccountException extends Exception{
    private final String ERROR_MESSAGE = "Error! Account not found.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
