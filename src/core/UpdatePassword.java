package core;

import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;
/**
 * An action to update a user's password in the system.
 * Ensures that the new password and confirmation password match before updating.
 */
public class UpdatePassword extends Action {
    private User user;
    private String newPasssword;
    private String confirmationPassword;
    private DatabaseSingleton db = DatabaseSingleton.getDatabase();


    /**
     * Gets the new password to be set.
     *
     * @return the new password
     */
    public String getNewPasssword() {
        return newPasssword;
    }

    /**
     * Sets the new password to be used.
     *
     * @param newPasssword the new password
     */
    public void setNewPasssword(String newPasssword) {
        this.newPasssword = newPasssword;
    }

    /**
     * Gets the confirmation password for verification.
     *
     * @return the confirmation password
     */
    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    /**
     * Sets the confirmation password for verification.
     *
     * @param confirmationPassword the confirmation password
     */
    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }
    /**
     * Gets the user whose password is being updated.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }
    /**
     * Sets the user whose password is being updated.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }
    /**
     * Prepares the action by verifying that the new password and confirmation match.
     *
     * @throws InvalidInputException if the passwords do not match
     * @throws InvalidAuthenticationException not thrown in this implementation
     */
    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {
        if (!newPasssword.equals(confirmationPassword)) {
            throw new InvalidInputException();
        }
    }
    /**
     * Executes the password update in the database.
     *
     * @throws InvalidAccountException if the user does not exist in the database
     * @throws InvalidAuthenticationException not thrown in this implementation
     */
    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        if (db.getUserByEmail(user.getEmail()) == null) {
            throw new InvalidAccountException();
        }
        user.setPassword(newPasssword);
        db.updateUserInfo(user);
    }
    /**
     * Authorizes the action.
     * In this implementation, no explicit authorization is required.
     *
     * @param user the user performing the action
     * @param account the account involved (not used)
     */
    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, InvalidAccountException {

    }
}
