package auth.core;

import auth.exceptions.InvalidInputException;

import java.util.Objects;

public abstract class User {
    protected String email;
    protected String password;

    protected void setPassword(String password) throws InvalidInputException {
        // TODO Password format verification (bellow nmb of characters, not-allowed symbols, etc...)
        if (false) {
            throw new InvalidInputException();
        }
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(email, user.email) ;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(password);
        return result;
    }
}
