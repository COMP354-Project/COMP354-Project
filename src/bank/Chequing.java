package bank;

import auth.core.Customer;
/**
 * Represents a Chequing account for a customer.
 * A Chequing account can hold funds and track transactions.
 * It does not have a credit limit like a Card account.
 */
public class Chequing extends Account {
    /**
     * Constructs a new Chequing account for the specified customer.
     *
     * @param customer the owner of this Chequing account
     */
    public Chequing(Customer customer) {
        super(customer);
    }
}

