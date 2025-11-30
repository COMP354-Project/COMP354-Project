package core;

import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import core.exceptions.InvalidInputException;
import bank.Account;
import database.DatabaseSingleton;
import database.exceptions.UserNotFoundException;

/**
 * An {@link Action} responsible for handling user login attempts.
 * <p>
 * This action validates the input credentials, checks the existence of a user
 * in the {@link database.DatabaseSingleton} user database, verifies the password,
 * and stores the authenticated {@link User} upon successful login.
 * </p>
 *
 * <h2>Workflow</h2>
 * <ol>
 *   <li>{@link #prepare()} verifies that both email and password were provided.</li>
 *   <li>{@link #execute()} retrieves the user by email and validates the password.</li>
 *   <li>No authorization step is required for login; {@link #authorize(User, Account)} is a no-op.</li>
 * </ol>
 *
 * <h2>Failure Conditions</h2>
 * <ul>
 *   <li>{@link core.exceptions.InvalidInputException} — empty email or password.</li>
 *   <li>{@link auth.exceptions.InvalidAuthenticationException} —
 *       thrown when the user cannot be found or when the password does not match.</li>
 * </ul>
 *
 * <h2>Successful Login</h2>
 * <p>
 * If no exception is thrown, {@link #execute()} sets the internal authenticated user,
 * which may be retrieved with {@link #getAuthenticatedUser()}.
 * </p>
 *
 * <h2>Notes</h2>
 * <ul>
 *   <li>This action does not perform any role-based authorization.</li>
 *   <li>Password comparison is plain-text; hashing should be implemented in production.</li>
 * </ul>
 *
 * @author Wang Mu Tian
 */
public class LoginAction extends Action {
    private String password;
    private String email;

    private User authentifiedUser;

    /**
     * Validates that both the email and password fields are non-empty.
     *
     * @throws InvalidInputException if the email or password field is empty.
     */
    @Override
    public void prepare() throws InvalidInputException {
        if (password.isEmpty() || email.isEmpty()) {
            throw new InvalidInputException();
        }
    }


    /**
     * Attempts to authenticate the user by:
     * <ol>
     *     <li>Fetching the user associated with the provided email.</li>
     *     <li>Comparing the provided password with the stored one.</li>
     * </ol>
     *
     * @throws InvalidAuthenticationException if the user does not exist
     *                                        or if the password is incorrect.
     */
    @Override
    public void execute() throws InvalidAuthenticationException {
        User user;
        // Verify user credentials with database
        try {
            user = DatabaseSingleton.getDatabase().getUserByEmail(email);
        } catch (UserNotFoundException exception) {
            throw new InvalidAuthenticationException(InvalidAuthenticationException.TYPE.USER_NOT_FOUND);
        }
        if (password == null || !password.equals(user.getPassword())) {
            throw new InvalidAuthenticationException(InvalidAuthenticationException.TYPE.WRONG_PASSWORD);
        }
        // No exception = Authentified
        // Assign authentified user
        this.authentifiedUser = user;
    }

    /**
     * Login does not require authorization because it does not involve account-level access.
     *
     * @param user    ignored
     * @param account ignored
     */
    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException {
    }

    /**
     * Sets the password for this user.
     *
     * @param password the new password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Sets the email for this user.
     *
     * @param email the new email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Returns the currently authenticated user associated with this instance.
     *
     * @return the authenticated user, or null if no user is authenticated
     */
    public User getAuthenticatedUser() {
        return authentifiedUser;
    }
}
