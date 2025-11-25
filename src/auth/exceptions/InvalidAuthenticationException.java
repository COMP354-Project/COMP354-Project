package auth.exceptions;


/**
 * Thrown to indicate that the provided email and password combination
 * does not match any existing user record in the database.
 */
public class InvalidAuthenticationException extends Exception {
    public InvalidAuthenticationException() {

    }

    public enum TYPE{
        USER_NOT_FOUND,
        WRONG_PASSWORD;
    }
    private TYPE invalidType;

    public InvalidAuthenticationException(TYPE type){
        this.invalidType = type;
    }

    public TYPE getInvalidType() {
        return invalidType;
    }

    private final String DEFAULT_ERROR_MESSAGE = "Error! Wrong credentials";

    @Override
    public String getMessage() {
        if (this.invalidType == TYPE.WRONG_PASSWORD){
            return "Error! Wrong password.";
        }else if(this.invalidType == TYPE.USER_NOT_FOUND){
            return "Error! User not found";
        }
        return DEFAULT_ERROR_MESSAGE;
    }
}
