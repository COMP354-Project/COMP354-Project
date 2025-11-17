package bank;

import auth.core.Customer;

public class Card extends Account {

    private double creditLimit; //Maximum

    private double creditUsage; //How much of maximum was spent

    public Card(Customer customer, double creditLimit) {
        super(customer);

        if (creditLimit < 0) {
            throw new IllegalArgumentException("Credit limit cannot be negative");
        }

        this.creditLimit = creditLimit;

        this.creditUsage = 0;
    }

    public void setCreditLimit(double newCreditLimit){
        if (newCreditLimit < 0) {
            throw new IllegalArgumentException("Credit limit cannot be negative");
        }
        this.creditLimit = newCreditLimit;
    }

    public double getCreditLimit(){
        return this.creditLimit;
    }

    public double getCreditUsage() {
        return creditUsage;
    }

    @Override
    public String toString() {
        return super.toString() + "Credit Limit: " + creditLimit + "\nCredit Usage: " + creditUsage + "\n";
    }
}

