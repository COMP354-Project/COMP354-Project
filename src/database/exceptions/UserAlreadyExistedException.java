package database.exceptions;

public class UserAlreadyExistedException extends RuntimeException {
    public UserAlreadyExistedException(String message) {
        super(message);
    }
}
