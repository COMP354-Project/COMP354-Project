package core;

import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;
import database.exceptions.UserAlreadyExistedException;

public class CreateUserAction extends Action {

    private User user;
    private DatabaseSingleton db = DatabaseSingleton.getDatabase();

    public final static String MESSAGE = "Account created successfully!";

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {

    }

    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        authorize(user, null);
        try {
            db.addUser(user);
        } catch (UserAlreadyExistedException e) {
            throw new InvalidAccountException();
        }
    }

    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, InvalidAccountException {

    }
}
