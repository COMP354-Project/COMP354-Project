package auth.core;


import auth.exceptions.InvalidInputException;

import java.util.Objects;


/**
 * Customer is a user that only has permission to view/edit on his own accounts.
 *
 */

public class Customer extends User {
    private String firstName;
    private String lastName;

    public Customer(String email, String password, String firstName, String lastName) {
        setEmail(email);
        try {
            setPassword(password);
        } catch (InvalidInputException e) {
            // This should never happen as password is already validated before creating a Customer
            System.out.println("Unexpected error: invalid password format.");
        }
//        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

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
        return "firstName=" + firstName +
               ", lastName=" + lastName;
    }
}

