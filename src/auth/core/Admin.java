package auth.core;

/**
 * Admin is a superuser of this system. It has access to every action in this banking system.
 * @see User
 *
 */
public class Admin extends User {

    public Admin(){
        super();
    }

    public Admin(String email, String password) {
        super(email, password);
    }
}
