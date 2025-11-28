package core;

import auth.core.Admin;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import bank.Transaction;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;
import database.exceptions.TransactionNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * An action to view transactions associated with a specific account
 * or all transactions if the user is an Admin and no account is specified.
 */
public class ViewTransactionAction extends Action {
    // Parameters
    /** The user performing the action. */
    User user;
    /** The account whose transactions are being viewed. */
    Account accountViewed;
    /** Database singleton for fetching transaction data. */
    DatabaseSingleton db = DatabaseSingleton.getDatabase();

    /** The resulting list of transactions after execution. */
    List<Transaction> listOfTransactions;

    /**
     * Gets the user performing the action.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user performing the action.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the account whose transactions are being viewed.
     *
     * @return the account
     */
    public Account getAccountViewed() {
        return accountViewed;
    }

    /**
     * Sets the account whose transactions are being viewed.
     *
     * @param accountViewed the account
     */
    public void setAccountViewed(Account accountViewed) {
        this.accountViewed = accountViewed;
    }

    /**
     * Gets the list of transactions fetched after execution.
     *
     * @return the list of transactions
     */
    public List<Transaction> getListOfTransactions() {
        return listOfTransactions;
    }

    /**
     * Prepares the action. Currently, no input validation is performed.
     *
     * @throws InvalidAuthenticationException never thrown
     * @throws InvalidInputException          never thrown
     */
    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {
    }

    /**
     * Executes the action to fetch transactions.
     * <p>
     * If the account is null and the user is an Admin, all transactions are fetched.
     * Otherwise, transactions for the specified account are fetched.
     * If no transactions are found, an empty list is returned.
     *
     * @throws InvalidAuthenticationException never thrown
     * @throws InvalidAccountException        never thrown
     */
    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        try {
            if (accountViewed == null && user != null && user instanceof Admin) {
                this.listOfTransactions = db.getTransactions();
            }
            else {
                this.listOfTransactions = db.getTransactionsByAccountID(accountViewed.getAccountID());
            }
        } catch (TransactionNotFoundException exception) {
            // Return empty list for display
            this.listOfTransactions = new ArrayList<Transaction>();
        }
    }

    /**
     * Authorizes the action.
     * <p>
     * Currently, no authorization logic is implemented for this action.
     *
     * @param user    the user performing the action
     * @param account the account involved (not used)
     * @throws InvalidAuthenticationException never thrown
     * @throws InvalidAccountException        never thrown
     */
    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, InvalidAccountException {

    }
}
