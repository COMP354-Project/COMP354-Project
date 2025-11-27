package core;

import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;

import java.util.ArrayList;

public class FetchUsersAction extends Action{


    DatabaseSingleton db = DatabaseSingleton.getDatabase();
    // Inputs
    private User user;

    // Outputs
    private ArrayList<User> userList;

    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {

    }

    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
    }

    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, InvalidAccountException {
        //
    }
}
