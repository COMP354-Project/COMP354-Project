package database.exceptions;

public class InvalidAccountIDException extends RuntimeException {
    public InvalidAccountIDException(String message) {
        super(message);
    }
}
