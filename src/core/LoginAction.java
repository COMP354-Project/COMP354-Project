package core;

import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import core.exceptions.InvalidInputException;
import bank.Account;
import database.DatabaseSingleton;

public class LoginAction extends Action {
    private String password;
    private String email;

    private User authentifiedUser;

    @Override
    public void prepare() throws InvalidInputException {
        if (password.isEmpty() || email.isEmpty()) {
            throw new InvalidInputException();
        }
        //^^^ is that it for the prepare()?
        // Yes
    }

    @Override
    public void execute() throws InvalidAuthenticationException {
        User user = DatabaseSingleton.getDatabase().getUserByEmail(email);
        if (user == null) {
            throw new InvalidAuthenticationException();
        }
        if (!user.getPassword().equals(password)) {
            throw new InvalidAuthenticationException();
        }

        // Assign authentified user
        this.authentifiedUser = user;
    }

    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException {
        // NO NEED TO AUTHORIZE
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
        return authentifiedUser;
    }
}
