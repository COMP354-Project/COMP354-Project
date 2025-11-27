package core;

import auth.core.Admin;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;
import database.exceptions.InvalidAccountIDException;

public class DeactivateAccountAction extends Action{
    private Account accountToDeactivate;
    private final User currentUser;
    private final DatabaseSingleton db = DatabaseSingleton.getDatabase();

    public DeactivateAccountAction(User currentUser, Account accountToDeactivate) {
        this.currentUser = currentUser;
        this.accountToDeactivate = accountToDeactivate;
    }

    public Account getAccountToDeactivate() {
        return accountToDeactivate;
    }

    void setAccountToDeactivate(Account accountToDeactivate) {
        this.accountToDeactivate = accountToDeactivate;
    }

    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {
        if (accountToDeactivate == null) {
            throw new InvalidInputException();
        }
    }

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

    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, InvalidAccountException {
        if (!(user instanceof Admin)){
            throw new InvalidAuthenticationException();
        }
    }
}
