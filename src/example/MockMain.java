package example;

import auth.core.Role;
import auth.exceptions.AuthentificationException;
import auth.exceptions.TimeOutException;
import auth.core.Customer;

public class MockMain {
    public static void main(String[] args) {
        /*
            --- Motivation for this structure of code ---
            - Flexible flow of execution - you can break down each step and prepare/execute at different times or in other main classes.
            - Asynchronous ready, thread ready, synchronization ready - each step is broken down or be further broken down into atomic slip of code.
                Then insert "synchronized" keyword on the function/code block to allow synchronization.
            - Everything you need for an action is encapsulated within the instance of an Action class. And! you can pass the action object around!
            - Data flows from front-end to back-end using Action -> prepare() -> execute() -> DatabaseSingleton.databaseDoSomething()
            - Result flows from back-end to front-end using Accessors, for example Action.getResult()
            - Errors/Exceptions are relayed from back-end to front-end till the appropriate interface catches it.
         */

        DivisionAction mockAction = new DivisionAction();
        // [....Preparing the division....]
        mockAction.setDividend(10);
        mockAction.setDivisor(5);
        try {
            mockAction.authorize(new Role() {
            }, new ExampleAccount(new Customer("Jack")));
        } catch (AuthentificationException e) {
            // Display bad authentification error, redirect flow of execution based on the activity diagram or use case diagram
            displayErrorGUI(e);
        }
        try {
            mockAction.prepare();
        } catch (TimeOutException e) {
            // Display timeout error, redirect flow of execution based on the activity diagram or use case diagram
            displayErrorGUI(e);
        } catch (DivisionByZeroException e){
            // Display division by zero error
            displayErrorGUI(e);
        }

        // [....Executing other stuff....]

        // [....Execute the division....]

        mockAction.execute();

        // [....Executing other stuff....]

        // [....Fetch the result....]
        int quotient = mockAction.getQuotient();
        int remainder = mockAction.getRemainder();

        // [....Executing other stuff....]

        // [....Display result....]
        System.out.println("Quotient=" + quotient + ", Remainder=" + remainder);
    }

    public static void displayErrorGUI(Exception e) {
        System.out.println(e.getMessage());
    }
}
