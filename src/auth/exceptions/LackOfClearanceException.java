package auth.exceptions;

public class LackOfClearanceException extends InvalidAuthenticationException {

    private final String DEFAULT_ERROR_MESSAGE = "Permission not granted!";


    @Override
    public String getMessage() {
        return DEFAULT_ERROR_MESSAGE;
    }
}
