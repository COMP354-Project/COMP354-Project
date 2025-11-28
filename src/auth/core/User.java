package auth.core;

import core.exceptions.InvalidInputException;

import java.util.Objects;

/**
 * Abstract class representing a system user with basic authentication details.
 * <p>
 * Each user has an email and a password. This class provides basic getters, setters,
 * and equality/hash code logic based on the email.
 * </p>
 */
public abstract class User {
    /** The email of the user, used as a unique identifier. */
    protected String email;

    /** The user's password. */
    protected String password;

    /**
     * Creates a new user with the specified email and password.
     *
     * @param email the user's email
     * @param password the user's password
     */
    public User(String email, String password) {
    }

    /**
     * Default constructor.
     */
    public User() {

    }

    /**
     * Sets the user's password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the user's email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Compares this user to another object for equality.
     * <p>
     * Two users are considered equal if they have the same email.
     * </p>
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(email, user.email) ;
    }

    /**
     * Returns the hash code for this user.
     * <p>
     * The hash code is computed from both email and password.
     * </p>
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int result = Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(password);
        return result;
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a string containing the email and password
     */
    public String toString(){
        return "User email: " + this.email + "\nUser password: " + this.password;
    }
}
