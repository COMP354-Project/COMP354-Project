package interfaces;

public abstract class Action implements Authentification{
    /***
     * Abstract function that prepare all the information and potentially authentification needed for the action to execute
     */
    abstract public void prepare();

    /***
     * Abstract function that execute the prepared function
     */
    abstract public void execute();
}
