package bank;

import auth.core.Customer;
import core.exceptions.InsufficientFundsException;
/**
 * Represents a credit card account for a customer.
 * A Card has a maximum credit limit and tracks how much of the credit has been used.
 */
public class Card extends Account {
    /** Maximum credit available for this card. */
    private double creditLimit; //Maximum
    /** Amount of credit currently used. */
    private double creditUsage; //How much of maximum was spent

    /**
     * Constructs a new Card for the specified customer with a given credit limit.
     *
     * @param customer the owner of the card
     * @param creditLimit the maximum credit allowed (must be >= 0)
     * @throws InsufficientFundsException if creditLimit is negative
     */
    public Card(Customer customer, double creditLimit) {
        super(customer);

        if (creditLimit < 0) {
            throw new InsufficientFundsException();
        }

        this.creditLimit = creditLimit;

        this.creditUsage = 0;
    }

    /**
     * Sets a new credit limit.
     *
     * @param newCreditLimit the new maximum credit allowed (must be >= 0)
     * @throws InsufficientFundsException if newCreditLimit is negative
     */
    public void setCreditLimit(double newCreditLimit){
        if (newCreditLimit < 0) {
            throw new InsufficientFundsException();
        }
        this.creditLimit = newCreditLimit;
    }

    /**
     * Sets the amount of credit used.
     *
     * @param creditUsage the current credit usage
     */
    public void setCreditUsage(double creditUsage){
        this.creditUsage = creditUsage;
    }

    /**
     * Returns the maximum credit limit of the card.
     *
     * @return credit limit
     */
    public double getCreditLimit(){
        return this.creditLimit;
    }

    /**
     * Returns the current credit usage of the card.
     *
     * @return credit usage
     */
    public double getCreditUsage() {
        return creditUsage;
    }

    /**
     * Returns a string representation of the card, including credit limit and usage.
     *
     * @return string representation of the card
     */
    @Override
    public String toString() {
        return super.toString() + "Credit Limit: " + creditLimit + "\nCredit Usage: " + creditUsage + "\n";
    }
}

