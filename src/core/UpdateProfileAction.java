package core;

import auth.core.Customer;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import auth.exceptions.LackOfClearanceException;
import bank.Account;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;

public class UpdateProfileAction extends Action{


    private Customer customer;
    private String FirstName;
    private String LastName;
    private DatabaseSingleton db = DatabaseSingleton.getDatabase();

    public void setCustomer(Customer customer){
        this.customer = customer;
    }
    public void setFirstName(String firstName){
        this.FirstName = firstName;
    }
    public void setLastName(String lastName){
        this.LastName = lastName;
    }

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

    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        customer.setFirstName(FirstName);
        customer.setLastName(LastName);

        db.updateUserInfo(customer);
    }

    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, LackOfClearanceException, InvalidAccountException {

    }


}
