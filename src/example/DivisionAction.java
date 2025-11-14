package example;

import auth.core.Role;
import auth.exceptions.AuthentificationException;
import auth.exceptions.TimeOutException;
import bank.Account;
import core.Action;

public class DivisionAction extends Action {
    private double dividend;
    private double divisor;
    private int quotient;
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
    public void authorize(Role role, Account account) throws AuthentificationException {
        /*
           Authentify user with an account in the database
           If valid, set isAuthorized to true
           If invalid, throw AuthentificationException
         */
        throw new AuthentificationException();
    }

    @Override
    public boolean isAuthorized() {
        return false;
    }

    public double getDividend() {
        return dividend;
    }

    public void setDividend(double dividend) {
        this.dividend = dividend;
    }

    public double getDivisor() {
        return divisor;
    }

    public void setDivisor(double divisor) {
        this.divisor = divisor;
    }

    public int getQuotient() {
        return quotient;
    }

    public void setQuotient(int quotient) {
        this.quotient = quotient;
    }

    public int getRemainder() {
        return remainder;
    }

    public void setRemainder(int remainder) {
        this.remainder = remainder;
    }
}
