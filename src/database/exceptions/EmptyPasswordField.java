package database.exceptions;

public class EmptyPasswordField extends RuntimeException {
    public EmptyPasswordField(String message) {
        super(message);
    }

    public EmptyPasswordField() {

    }
}
