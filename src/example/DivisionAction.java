package example;

import auth.AuthentificationException;
import auth.TimeOutException;
import bank.Account;
import core.Action;
import auth.User;
import lombok.Getter;
import lombok.Setter;

public class DivisionAction extends Action {
    @Getter
    @Setter
    private double dividend;
    @Getter
    @Setter
    private double divisor;
    @Getter
    private int quotient;
    @Getter
    private int remainder;

    private boolean isAuthorized;

    @Override
    public void prepare() throws DivisionByZeroException, TimeOutException {
        // Division by zero!
        if (divisor == 0) {
            throw new RuntimeException();
        }

        if (!isAuthorized) {
            // Sometimes the connection times out, thus revisit the authentification of the user
            throw new TimeOutException();
        }
    }

    @Override
    public void execute() {
        double result = (dividend / divisor);
        this.quotient = (int) result;
        this.remainder = (int) (divisor * (result - quotient));
    }

    @Override
    public void authorize(User user, Account account) throws AuthentificationException {
        /*
           Authentify user with an account in the database
           If valid, set isAuthorized to true
           If invalid, throw AuthentificationException
         */
        throw new AuthentificationException();
    }

    @Override
    public void isAuthorized() {

    }
}
