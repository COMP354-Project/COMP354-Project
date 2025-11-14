package core;

import auth.Authorizable;
import auth.core.Customer;
import auth.core.Role;
import auth.exceptions.AuthentificationException;
import auth.exceptions.TimeOutException;
import bank.Account;
import bank.Branch;
import bank.Transaction;

import javax.naming.AuthenticationException;

public class TransactionAction extends Action {


    //Data needed to prepare
    private Customer user;
    private Account sourceAccount;
    private Account destinationAccount; //Optional for destination
    private Transaction transactionDetails;
    private Branch branch; //use to check for fraud?



    //Flags
    private boolean authorized;
    private boolean prepared;
    private boolean executed;

    //Debug/Output message
    private String resultMessage;

    //Setup phase
    public void setUser(Customer user){
        this.user = user;
    }

    public void setSourceAccount(Account sourceAccount){
        this.sourceAccount = sourceAccount;
    }

    public void setDestinationAccount(Account destinationAccount){
        this.destinationAccount = destinationAccount;
    }

    public void setTransactionDetails(Transaction t){
        this.transactionDetails = transactionDetails;
    }

    public void setBranch(Branch branch){
        this.branch = branch;
    }


    //Preparation phase
    @Override
    public void prepare() throws AuthenticationException {
        //Flag check
        if (!isAuthorized()){
            throw new TimeOutException("System timed out.");
        }
        //Validate data
        if (user == null || sourceAccount == null || transactionDetails == null){
            throw new IllegalStateException();
        }


        double amount = transactionDetails.getAmount();

        if (amount <= 0){
            throw new IllegalArgumentException();
        }
        if (sourceAccount.getBalance() < 0){
            throw new RuntimeException();
        }
        if (destinationAccount == null ){
            throw new RuntimeException();
        }


        prepared = true;
    }

    @Override
    public void execute() {
        if (!prepared){
            throw new IllegalStateException();
        }

        double amt = transactionDetails.getAmount();

        executed = true;
    }

    @Override
    public void authorize(Role role, Account account) throws AuthentificationException, AuthenticationException {
        //checks the role and account comparing?

        throw new AuthenticationException();
    }

    @Override
    public boolean isAuthorized() {
        return authorized;
    }

    public boolean wasExecuted(){
        return executed;
    }

    public String getResultMessage(){
        return resultMessage;
    }
}
