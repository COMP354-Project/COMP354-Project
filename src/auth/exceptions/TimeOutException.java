package auth.exceptions;

public class TimeOutException extends RuntimeException{
    private final String ERROR_MESSAGE = "System timed out.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }

    // TODO: Implement attributes and message display (toString) for this exception.
}
