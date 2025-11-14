package core;

import auth.Authorizable;
import auth.exceptions.InvalidAuthenticationException;
import auth.exceptions.InvalidInputException;
import database.DatabaseSingleton;

/**
 * An action is an operation of the banking system (similar to an use case)
 */
public abstract class Action implements Authorizable {

    protected AUTH_STATUS authorized;

    /***
     * Abstract function that fetch attributes from an instance of an action and prepare all data needed for execution.
     * An action is an operation that encapsulate everything it needs: data, functions, and injection of other utility classes.
     * @see DatabaseSingleton
     */
    abstract public void prepare() throws InvalidAuthenticationException, InvalidInputException;

    /***
     * Abstract function that execute the prepared action. Can be synchronized
     */
    abstract public void execute() throws InvalidAuthenticationException;

    @Override
    public boolean isAuthorized() {
        return (authorized.equals(AUTH_STATUS.AUTHORIZED));
    }
}
