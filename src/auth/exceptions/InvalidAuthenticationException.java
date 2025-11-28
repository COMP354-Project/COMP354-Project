package auth.exceptions;


/**
 * Thrown to indicate that the provided email and password combination
 * does not match any existing user record in the database.
 * <p>
 * This exception can specify the type of authentication failure using the {@link TYPE} enum:
 * <ul>
 *     <li>{@link TYPE#USER_NOT_FOUND}: No user exists with the provided email.</li>
 *     <li>{@link TYPE#WRONG_PASSWORD}: The password provided is incorrect.</li>
 * </ul>
 * </p>
 */
public class InvalidAuthenticationException extends Exception {
    /** The type of invalid authentication that occurred. */
    public InvalidAuthenticationException() {

    }
    /** Default error message if no type is specified. */
    public enum TYPE{
        USER_NOT_FOUND,
        WRONG_PASSWORD;
    }
    /** Enum representing the possible types of authentication failure. */
    private TYPE invalidType;
    /** Constructs a generic InvalidAuthenticationException with no specific type. */
    public InvalidAuthenticationException(TYPE type){
        this.invalidType = type;
    }
    /**
     * Constructs an InvalidAuthenticationException with a specific type.
     *
     * @param {type} the type of authentication failure
     */
    public TYPE getInvalidType() {
        return invalidType;
    }
    /**
     * Returns the type of authentication failure that caused this exception.
     *
     * @return the  {TYPE} of failure
     */
    private final String DEFAULT_ERROR_MESSAGE = "Error! Wrong credentials";

    /**
     * Returns the detailed message of this exception based on the failure type.
     *
     * @return the error message corresponding to the type of failure
     */
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
