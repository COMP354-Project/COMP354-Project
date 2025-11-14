package auth.exceptions;

public class DuplicateAccountException extends RuntimeException {
    private final String ERROR_MESSAGE = "Duplicate!";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
