package auth.core;

/**
 * Represents an Admin in the banking system.
 * <p>
 * An Admin is a superuser with full access to all actions and data in the system.
 * </p>
 *
 * @see User
 */
public class Admin extends User {
    /**
     * Default constructor. Creates an Admin with no email or password.
     */
    public Admin(){
        super();
    }
    /**
     * Constructs an Admin with the specified email and password.
     *
     * @param email    the email of the admin
     * @param password the password of the admin
     */
    public Admin(String email, String password) {
        super(email, password);
    }
}
