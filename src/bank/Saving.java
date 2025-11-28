package bank;

import auth.core.Customer;
/**
 * Represents a Saving account for a customer.
 * A Saving account can hold funds and track transactions.
 * Typically, it may accrue interest (not implemented here).
 */
public class Saving extends Account {
    /**
     * Constructs a new Saving account for the specified customer.
     *
     * @param customer the owner of this Saving account
     */
    public Saving(Customer customer) {
        super(customer);
    }
}
