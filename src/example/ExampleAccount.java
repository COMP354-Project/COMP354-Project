package example;

import bank.Account;
import auth.core.Customer;

public class ExampleAccount extends Account {
    private int accountId;
    public ExampleAccount(Customer customer) {
        super(customer);

    }
}
