package core;

import auth.Authorizable;
import auth.exceptions.InvalidAuthenticationException;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;

/**
 * An action is an operation of the banking system (similar to an use case)
 */
public abstract class Action implements Authorizable {
    /**
     * Indicates the authorization status of the current action.
     * <p>
     * This value is set during {@code prepare()} to determine whether the user
     * has sufficient permissions to execute the requested action.
     * </p>
     */
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
    abstract public void execute() throws InvalidAuthenticationException, InvalidAccountException;

    /**
     * Returns whether the action is authorized to be executed.
     *
     * @return true if the action is authorized; false otherwise
     */
    @Override
    public boolean isAuthorized() {
        return (authorized.equals(AUTH_STATUS.AUTHORIZED));
    }
}
