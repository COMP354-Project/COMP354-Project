package auth.core;


import core.exceptions.InvalidInputException;

import java.util.Objects;

/**
 * Represents a Customer in the system.
 * <p>
 * A Customer is a type of User that only has permission to view or edit their own accounts.
 * Stores personal information such as first name and last name.
 * </p>
 */
public class Customer extends User {
    /** The first name of the customer. */
    private String firstName;

    /** The last name of the customer. */
    private String lastName;

    /**
     * Constructs a Customer with the specified email, password, first name, and last name.
     *
     * @param email      the email of the customer
     * @param password   the password of the customer
     * @param firstName  the first name of the customer
     * @param lastName   the last name of the customer
     */
    public Customer(String email, String password, String firstName, String lastName) {
        setEmail(email);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
    }

    /** Default constructor. */
    public Customer() {

    }

    /**
     * Returns the first name of the customer.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the customer.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the customer.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the customer.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Customer customer = (Customer) o;
        return Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(firstName);
        result = 31 * result + Objects.hashCode(lastName);
        return result;
    }

    public String toString() {
        return super.toString() + "\nFirst Name: " + firstName +
                "\nLast Name: " + lastName;
    }
}

