package auth;

import bank.Account;

import javax.naming.AuthenticationException;

public interface Authorizable {
    void authorize(User user, Account account) throws AuthentificationException, AuthenticationException;
    void isAuthorized();
}
