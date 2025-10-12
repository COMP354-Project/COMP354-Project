package example;

import bank.Account;
import bank.Customer;

public class ExampleAccount extends Account {
    private int accountId;
    public ExampleAccount(Customer customer) {
        super(customer);

    }

    @Override
    public void pay() {

    }

    @Override
    public void receipt() {

    }
}
