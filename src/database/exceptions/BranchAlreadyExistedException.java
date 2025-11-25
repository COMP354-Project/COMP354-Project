package database.exceptions;

public class BranchAlreadyExistedException extends RuntimeException {
    public BranchAlreadyExistedException(String message) {
        super(message);
    }
}
