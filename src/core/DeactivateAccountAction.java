package core;

import auth.core.Admin;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;
import database.exceptions.InvalidAccountIDException;

/**
 * Action to deactivate a bank account.
 * <p>
 * Only an {@link Admin} user is authorized to perform this action.
 * The account status will be set to {@link Account.AccountStatus#INACTIVE}.
 */
public class DeactivateAccountAction extends Action{
    /** The account to be deactivated. */
    private Account accountToDeactivate;

    /** The user performing this action. */
    private final User currentUser;

    /** Reference to the database singleton. */
    private final DatabaseSingleton db = DatabaseSingleton.getDatabase();

    /**
     * Constructs the action with the current user and target account.
     *
     * @param currentUser the user performing the action
     * @param accountToDeactivate the account to deactivate
     */
    public DeactivateAccountAction(User currentUser, Account accountToDeactivate) {
        this.currentUser = currentUser;
        this.accountToDeactivate = accountToDeactivate;
    }

    /**
     * Returns the account that will be deactivated.
     *
     * @return the account to deactivate
     */
    public Account getAccountToDeactivate() {
        return accountToDeactivate;
    }

    /**
     * Sets the account to deactivate.
     *
     * @param accountToDeactivate the account to deactivate
     */
    void setAccountToDeactivate(Account accountToDeactivate) {
        this.accountToDeactivate = accountToDeactivate;
    }

    /**
     * Prepares the action by validating the input account.
     *
     * @throws InvalidInputException if the account to deactivate is null
     * @throws InvalidAuthenticationException never thrown in this method
     */
    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {
        if (accountToDeactivate == null) {
            throw new InvalidInputException();
        }
    }

    /**
     * Executes the action to deactivate the account.
     * <p>
     * The current user must be authorized (Admin). The account status
     * is updated in the database.
     *
     * @throws InvalidAuthenticationException if the user is not authorized
     * @throws InvalidAccountException if the account cannot be updated
     */
    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        try {
            authorize(currentUser, accountToDeactivate);}
        catch (InvalidAuthenticationException e) {
            System.err.println(e.getMessage());
        }
        accountToDeactivate.setActivity(Account.AccountStatus.INACTIVE);
        try {
            db.updateAccount(accountToDeactivate);
        } catch (InvalidAccountIDException e) {
            throw new InvalidAccountException();
        }
    }

    /**
     * Checks if the given user is authorized to perform this action on the account.
     *
     * @param user the user performing the action
     * @param account the account to be deactivated
     * @throws InvalidAuthenticationException if the user is not an Admin
     * @throws InvalidAccountException never thrown in this method
     */
    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, InvalidAccountException {
        if (!(user instanceof Admin)){
            throw new InvalidAuthenticationException();
        }
    }
}
