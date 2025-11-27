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

public class ViewTransactionAction extends Action {
    // Parameters
    User user;
    Account accountViewed;
    DatabaseSingleton db = DatabaseSingleton.getDatabase();

    // Results
    List<Transaction> listOfTransactions;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccountViewed() {
        return accountViewed;
    }

    public void setAccountViewed(Account accountViewed) {
        this.accountViewed = accountViewed;
    }

    public List<Transaction> getListOfTransactions() {
        return listOfTransactions;
    }

    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {
    }

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


    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, InvalidAccountException {

    }
}
