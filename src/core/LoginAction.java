package core;

import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import auth.exceptions.InvalidInputException;
import bank.Account;
import database.DatabaseSingleton;

public class LoginAction extends Action{
    private String password;
    private String email;

    private User user;

    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {
    }

    @Override
    public void execute() throws InvalidAuthenticationException {
        DatabaseSingleton.getDataBase();

        // IF NOT FOUND IN DATABASE
        throw new InvalidAuthenticationException();
        // Mix and match in database
    }

    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getAuthenticatedUser() {
        return user;
    }
}
