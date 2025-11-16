package core;

import auth.core.Customer;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import auth.exceptions.InvalidInputException;
import auth.exceptions.TimeOutException;
import bank.Account;
import bank.Branch;
import bank.Transaction;

import javax.management.relation.Role;
import javax.naming.AuthenticationException;

public class TransactionAction extends Action {
    //Data needed to prepare
    private User user;
    private Account sourceAccount;
    private Account destinationAccount; //Optional for destination
    private Transaction transactionDetails;
    private Branch branch; //use to check for fraud?


    //Flags
    private boolean prepared;
    private boolean executed;

    //Debug/Output message
    private String resultMessage;

    //Setup phase
    public void setUser(Customer user) {
        this.user = user;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public void setTransactionDetails(Transaction t) {
        this.transactionDetails = transactionDetails;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }


    //Preparation phase
    @Override
    public void prepare() throws InvalidAuthenticationException, TimeOutException, InvalidInputException {
        //Validate inputted data
        if (user == null || sourceAccount == null || transactionDetails == null) {
            throw new InvalidInputException();
        }

        //Flag check
        // Authorize the action
        authorize(user, sourceAccount);

        double amount = transactionDetails.getAmount();
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        if (sourceAccount.getBalance() < 0) {
            throw new RuntimeException();
        }
        if (destinationAccount == null) {
            throw new RuntimeException();
        }
        prepared = true;
    }

    @Override
    public void execute() {
        if (!prepared) {
            throw new IllegalStateException();
        }
        double amt = transactionDetails.getAmount();
        executed = true;
    }

    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException {
        if (user == null || account == null) {
            throw new InvalidAuthenticationException();
        }
        // Case: if user is a customer
        if (user instanceof Customer) {
            if (account.getCustomer().equals(user)) {
                authorized = AUTH_STATUS.AUTHORIZED;
            }
        }
        throw new InvalidAuthenticationException();
    }


    public boolean wasExecuted() {
        return executed;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}
