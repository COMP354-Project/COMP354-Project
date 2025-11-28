package core;

import auth.core.Customer;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import auth.exceptions.LackOfClearanceException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;
/**
 * An action to update a customer's profile information (first and last name).
 * <p>
 * This action ensures that the provided customer and new name values are valid before updating.
 */
public class UpdateProfileAction extends Action{


    private Customer customer;
    private String FirstName;
    private String LastName;
    private DatabaseSingleton db = DatabaseSingleton.getDatabase();

    /**
     * Sets the customer whose profile is to be updated.
     *
     * @param customer the customer instance
     */
    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    /**
     * Sets the new first name for the customer.
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName){
        this.FirstName = firstName;
    }

    /**
     * Sets the new last name for the customer.
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName){
        this.LastName = lastName;
    }

    /**
     * Prepares the action by validating the input fields.
     *
     * @throws InvalidAuthenticationException if the customer is null
     * @throws InvalidInputException          if first or last name is null or blank
     */
    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {
        if (customer == null){
            throw new InvalidAuthenticationException();
        }
        if (FirstName == null || FirstName.isBlank()){
            throw new InvalidInputException();
        }
        if (LastName == null || LastName.isBlank()){
            throw new InvalidInputException();
        }
    }

    /**
     * Executes the profile update in the database.
     *
     * @throws InvalidAuthenticationException not thrown in this implementation
     * @throws InvalidAccountException        not thrown in this implementation
     */
    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        customer.setFirstName(FirstName);
        customer.setLastName(LastName);

        db.updateUserInfo(customer);
    }

    /**
     * Authorizes the action.
     * <p>
     * Currently, no authorization logic is implemented for this action.
     *
     * @param user    the user performing the action
     * @param account the account involved (not used)
     */
    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, LackOfClearanceException, InvalidAccountException {

    }


}
