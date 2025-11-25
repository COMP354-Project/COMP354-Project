package database.exceptions;

public class InvalidTransactionIDException extends RuntimeException {
    public InvalidTransactionIDException(String message) {
        super(message);
    }
}
