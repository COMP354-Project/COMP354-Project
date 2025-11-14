package auth;

import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import core.Action;

public interface Authorizable {

    enum AUTH_STATUS{
        AUTHORIZED,
        NOT_AUTHORIZED;
    }

    /**
     * Authentify the user by matching email and password within database.
     * Then the user has permission to utilize various actions within the permissions of his role.
     * @param user    User account.
     * @param account A banking account.
     * @throws InvalidAuthenticationException Throws when user enters the wrong identification info (email,password).
     * @see Action
     */
    void authorize(User user, Account account) throws InvalidAuthenticationException;

    /**
     * @return Return "true" is the user has already inputted the information and is authorized to execute an action.
     * Return "false" if the user hasn't inputted the information or has inputted wrong information, thus is not authorized to execute an action.
     * @see Authorizable
     */
    boolean isAuthorized();
}
