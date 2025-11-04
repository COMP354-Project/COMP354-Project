package auth;

import auth.core.User;
import auth.exceptions.UserAuthenticationException;
import bank.Account;
import core.Action;

public interface Authorizable {

    final boolean STATUS_AUTHORIZED = true;
    final boolean STATUS_NOT_AUTHORIZED = false;

    /**
     * Authentify the user by matching email and password within database.
     * Then the user has permission to utilize various actions within the permissions of his role.
     * @param user    User account.
     * @param account A banking account.
     * @throws UserAuthenticationException Throws when user enters the wrong identification info (email,password).
     * @see Action
     */
    void authorize(User user, Account account) throws UserAuthenticationException;

    /**
     * @return Return "true" is the user has already inputted the information and is authorized to execute an action.
     * Return "false" if the user hasn't inputted the information or has inputted wrong information, thus is not authorized to execute an action.
     * @see Authorizable
     */
    boolean isAuthorized();
}
