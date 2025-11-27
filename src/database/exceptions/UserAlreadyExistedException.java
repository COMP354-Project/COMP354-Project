package database.exceptions;

public class UserAlreadyExistedException extends Exception {
    public UserAlreadyExistedException(String message) {
        super(message);
    }
}
