package core;

import auth.core.Customer;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;

import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;
/**
 * ProfileAction is responsible for fetching the accounts associated with a user.
 * It ensures that only a Customer (or a teller if tellerFlag is set) can execute this action.
 */
public class ProfileAction extends Action {

    private User currentUser;
    private ArrayList<Account> userAccount;

    private boolean tellerFlag;

    /**
     * Prepares the action by validating the current user.
     *
     * @throws InvalidAuthenticationException if authentication fails
     * @throws InvalidInputException if the user is not a Customer and tellerFlag is false
     */
    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {
        if (!(currentUser instanceof Customer) && !tellerFlag){ //might need to be removed
            throw new InvalidInputException(); //might change later
        }

    }
    /**
     * Executes the action to fetch all accounts associated with the current user.
     *
     * @throws InvalidAuthenticationException if currentUser is null
     * @throws InvalidAccountException if account retrieval fails
     */
    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        if (currentUser == null){
            throw new InvalidAuthenticationException();
        }
        this.userAccount = DatabaseSingleton.getDatabase().getAccountsByEmail(currentUser.getEmail());

    }
    /**
     * Authorizes the action.
     * Currently, authorization is assumed to be handled beforehand.
     *
     * @param user the user performing the action
     * @param account an account (not used in this implementation)
     * @throws InvalidAuthenticationException if authorization fails
     * @throws InvalidAccountException if account is invalid
     */
    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, InvalidAccountException {
        //should already be authorized beforehand
    }
    /**
     * Sets the user whose profile and accounts are to be accessed.
     *
     * @param user the current user
     */
    public void setCurrentUser(User user){
        this.currentUser = user;
    }
    /**
     * Returns the current user.
     *
     * @return the current user
     */
    public User getCurrentUser(){
        return this.currentUser;
    }
    /**
     * Returns the accounts associated with the current user.
     *
     * @return a list of accounts
     */
    public ArrayList<Account> getUserAccount(){
        return this.userAccount;
    }
    /**
     * Sets a flag indicating whether the action is being performed by a teller.
     *
     * @param tellerFlag true if the user is a teller, false otherwise
     */
    public void setTellerFlag(boolean tellerFlag) {
        this.tellerFlag = tellerFlag;
    }
}
