package core;

import auth.core.Admin;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import auth.exceptions.LackOfClearanceException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;
import database.exceptions.UserAlreadyExistedException;


/**
 * Action responsible for creating a new user in the system.
 *
 * <p>Only admins are authorized to perform this action. Upon execution,
 * the user is added to the database if they do not already exist.</p>
 */
public class CreateUserAction extends Action {
    private final DatabaseSingleton db = DatabaseSingleton.getDatabase();
    // Inputs
    private User newUser;
    private User user;
    // Outputs
    public final static String MESSAGE = "Account created successfully!";

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {

    }


    /**
     * Creates the user after authorization.
     *
     * @throws InvalidAuthenticationException if authentication fails, or user doesn't have permission
     * @throws InvalidAccountException        if the user already exists
     */
    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        authorize(user, null);
        try {
            db.addUser(newUser);
        } catch (UserAlreadyExistedException e) {
            throw new InvalidAccountException();
        }
    }

    /**
     * Ensures only Admin users may perform this action.
     *
     * @throws InvalidAuthenticationException if the caller is not an admin
     *                                        (LackOfClearanceException)
     */
    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, LackOfClearanceException, InvalidAccountException {
        if (user.getClass().equals(Admin.class)) {
            return;
        }
        throw new LackOfClearanceException();
    }
}
