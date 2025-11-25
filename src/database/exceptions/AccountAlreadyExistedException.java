package database.exceptions;

public class AccountAlreadyExistedException extends RuntimeException {
    public AccountAlreadyExistedException(String message) {
        super(message);
    }
}
