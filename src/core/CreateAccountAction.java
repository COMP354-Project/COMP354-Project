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

public class CreateAccountAction extends Action {
    DatabaseSingleton db = DatabaseSingleton.getDatabase();
    // Inputs
    private User user;
    private User associatedUser;
    private String accountType;
    // Outputs
    public final static String MESSAGE = "Account created successfully!";

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getAssociatedUser() {
        return associatedUser;
    }

    public void setAssociatedUser(User associatedUser) {
        this.associatedUser = associatedUser;
    }

    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {

    }

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

    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, LackOfClearanceException, InvalidAccountException {
        if (!(user instanceof Admin)) {
            throw new InvalidAuthenticationException();
        }
    }
}
