package core;

import auth.core.Customer;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;

import javax.management.InvalidAttributeValueException;

public class ProfileAction extends Action {

    private User currentUser;
    private Account userAccount;

    private boolean tellerFlag;


    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {
        if (!(currentUser instanceof Customer) && !tellerFlag){ //might need to be removed
            throw new InvalidInputException(); //might change later
        }
        if (userAccount == null){
            throw new InvalidInputException(); //might change later
        }

    }

    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        Account account = DatabaseSingleton.getDatabase().getAccountByUser(currentUser);
        if (account == null){
            throw new InvalidAccountException();
        }
        this.userAccount = account;
    }

    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, InvalidAccountException {
        //should already be authorized beforehand
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }
    public User getCurrentUser(){
        return this.currentUser;
    }
    public Account getUserAccount(){
        return this.userAccount;
    }

    public void setTellerFlag(boolean tellerFlag) {
        this.tellerFlag = tellerFlag;
    }
}
