package core;

import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;

public class UpdatePassword extends Action {
    private User user;
    private String newPasssword;
    private String confirmationPassword;
    private DatabaseSingleton db = DatabaseSingleton.getDatabase();

    public String getNewPasssword() {
        return newPasssword;
    }

    public void setNewPasssword(String newPasssword) {
        this.newPasssword = newPasssword;
    }


    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {
        if (!newPasssword.equals(confirmationPassword)) {
            throw new InvalidInputException();
        }
    }

    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        if (db.getUserByEmail(user.getEmail()) == null) {
            throw new InvalidAccountException();
        }
        user.setPassword(newPasssword);
        db.updateUserInfo(user);
    }

    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, InvalidAccountException {

    }
}
