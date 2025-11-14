package auth;

import auth.core.Role;
import auth.exceptions.AuthentificationException;
import bank.Account;

import javax.naming.AuthenticationException;

public interface Authorizable {
    void authorize(Role role, Account account) throws AuthentificationException, AuthenticationException;
    boolean isAuthorized();
}
