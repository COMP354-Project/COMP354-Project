package core;

import auth.core.Admin;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import auth.exceptions.LackOfClearanceException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidEmailException;
import core.exceptions.InvalidInputException;
import core.exceptions.InvalidPasswordException;
import database.DatabaseSingleton;
import database.exceptions.UserAlreadyExistedException;


/**
 * Action responsible for creating a new user in the system.
 *
 * <p>Only admins are authorized to perform this action. Upon execution,
 * the user is added to the database if they do not already exist.</p>
 */
public class CreateUserAction extends Action {
    /**
     * Reference to the database singleton.
     */
    private final DatabaseSingleton db = DatabaseSingleton.getDatabase();
    // Inputs
    /**
     * The new user to be created.
     */
    private User newUser;
    /**
     * The user performing this action.
     */
    private User user;
    // Outputs
    /**
     * Success message for account creation.
     */
    public final static String MESSAGE = "Account created successfully!";

    /**
     * Returns the user performing this action.
     *
     * @return the {@link User} performing this action
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user performing this action.
     *
     * @param user the {@link User} performing the action
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the new user to be created.
     *
     * @return the {@link User} being added
     */
    public User getNewUser() {
        return newUser;
    }

    /**
     * Sets the new user to be created.
     *
     * @param newUser the {@link User} to add to the system
     */
    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public boolean validEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public boolean validPassword(String password) {
        // Password must be at least 8 characters long and contain at least one uppercase letter,
        // one lowercase letter, one digit, and one special character
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordRegex);
    }

    /**
     * Prepares the action by validating inputs.
     *
     * @throws InvalidAuthenticationException if the user is not authenticated
     * @throws InvalidInputException          if the new user data is invalid
     */
    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException, InvalidEmailException, InvalidPasswordException {
        // Here we check for valid inputs
        if (user == null || newUser == null) {
            throw new InvalidInputException();
        }
        if (!validEmail(newUser.getEmail())) {
            throw new InvalidEmailException("Invalid email format.");
        }
        if (!validPassword(newUser.getPassword())) {
            throw new InvalidPasswordException("Password must be at least 8 characters long and include uppercase, lowercase, digit, and special character.");
        }
    }


    /**
     * Creates the user after authorization.
     *
     * @throws InvalidAuthenticationException if authentication fails, or user doesn't have permission
     * @throws InvalidAccountException        if the user already exists
     */
    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException, InvalidPasswordException, InvalidEmailException {
        authorize(user, null);
        try {
            prepare();
        } catch (InvalidEmailException e) {
            throw new InvalidEmailException(e.getMessage());
        } catch (InvalidPasswordException e) {
            throw new InvalidPasswordException(e.getMessage());
        } catch (InvalidInputException e) {
            throw new InvalidAccountException();
        }
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
