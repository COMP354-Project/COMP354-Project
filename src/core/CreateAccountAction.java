package core;

import auth.core.Admin;
import auth.core.Customer;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import auth.exceptions.LackOfClearanceException;
import bank.Account;
import bank.Card;
import bank.Chequing;
import bank.Saving;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;

/**
 * Represents the action of creating a new account in the banking system.
 * <p>
 * This action is typically executed by an {@link Admin} and allows
 * creating accounts for a {@link Customer} with specified account type
 * ("Chequing", "Saving", or "Card").
 */
public class CreateAccountAction extends Action {
    DatabaseSingleton db = DatabaseSingleton.getDatabase();
    // Inputs
    private User user;
    private User associatedUser;
    private String accountType;
    // Outputs
    /** Console message confirmation*/
    public final static String MESSAGE = "Account created successfully!";

    /**
     * Returns the type of account to be created.
     *
     * @return the account type as a String
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the type of account to be created.
     *
     * @param accountType the account type ("Chequing", "Saving", "Card")
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * Returns the user performing the action.
     *
     * @return the {@link User} performing this action
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user performing the action.
     *
     * @param user the {@link User} performing this action
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the user for whom the account is being created.
     *
     * @return the associated {@link User}
     */
    public User getAssociatedUser() {
        return associatedUser;
    }

    /**
     * Sets the user for whom the account is being created.
     *
     * @param associatedUser the {@link User} to associate with the new account
     */
    public void setAssociatedUser(User associatedUser) {
        this.associatedUser = associatedUser;
    }

    /**
     * Prepares the action by performing any necessary data validation.
     *
     * @throws InvalidAuthenticationException if the user is not authenticated
     * @throws InvalidInputException if the input data is invalid
     */
    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {

    }

    /**
     * Executes the action by creating the specified account for the associated user.
     *
     * @throws InvalidAuthenticationException if the user is not authenticated
     * @throws InvalidAccountException if the associated user or account creation fails
     */
    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        Account account = null;
        if (associatedUser instanceof Customer) {
            if (accountType.equals("Chequing")) {
                account = new Chequing((Customer) associatedUser);
            }
            if (accountType.equals("Saving")) {
                account = new Saving((Customer) associatedUser);
            }
            if (accountType.equals("Card")) {
                account = new Card((Customer) associatedUser,2000);
            }
            db.addAccount(account);
        }
    }
    /**
     * Authorizes the action to ensure the user has clearance to create accounts.
     *
     * @param user the user attempting the action
     * @param account not used in this action but required by interface
     * @throws InvalidAuthenticationException if the user is not an Admin
     * @throws LackOfClearanceException never thrown here but part of signature
     * @throws InvalidAccountException never thrown here but part of signature
     */
    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, LackOfClearanceException, InvalidAccountException {
        if (!(user instanceof Admin)) {
            throw new InvalidAuthenticationException();
        }
    }
}
