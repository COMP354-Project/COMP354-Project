package database.exceptions;

public class TransactionAlreadyExistedException extends RuntimeException {
    public TransactionAlreadyExistedException(String message) {
        super(message);
    }
}
