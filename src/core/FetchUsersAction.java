package core;

import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;

import java.util.ArrayList;
/**
 * Action to fetch a list of users from the system.
 * <p>
 * Only authorized users can perform this action. This class provides
 * placeholders for input (the current user) and output (the list of users).
 */
public class FetchUsersAction extends Action{

    /** Database instance for accessing user records. */
    DatabaseSingleton db = DatabaseSingleton.getDatabase();
    // Inputs
    /** The user performing the fetch operation. */
    private User user;

    // Outputs
    private ArrayList<User> userList;

    /**
     * Sets the user performing this action.
     *
     * @param user the user performing the fetch
     */
    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {

    }

    /**
     * Executes the action, fetching users from the database.
     *
     * @throws InvalidAuthenticationException if the user is not authorized
     * @throws InvalidAccountException if an account-related error occurs during fetch
     */
    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
    }
    /**
     * Authorizes the user to perform this action.
     *
     * @param user the user performing the action
     * @param account the account to check (not used in this action)
     * @throws InvalidAuthenticationException if the user is not authorized
     * @throws InvalidAccountException if the account is invalid
     */
    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, InvalidAccountException {
        //
    }
}
