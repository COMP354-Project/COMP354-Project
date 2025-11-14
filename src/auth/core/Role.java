package auth.core;

import auth.exceptions.PasswordFormatException;

public abstract class Role {
    protected String email;
    protected String password;


    protected void setPassword(String password) throws PasswordFormatException {
        // TODO Password format verification (bellow nmb of characters, not-allowed symbols, etc...)
        if (false) {
            throw new PasswordFormatException();
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
}
