package core;

import auth.core.Customer;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import core.exceptions.InsufficientFundsException;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import auth.exceptions.TimeOutException;
import bank.Account;
import bank.Branch;
import bank.Transaction;
import database.DatabaseSingleton;

public class ExecuteTransactionAction extends Action {
    // Initialize database
    DatabaseSingleton db;
    //Data needed to prepare
    private User user;
    private Transaction transactionDetails;
    private Branch branch; //use to check for fraud? not used for anything for now

    public ExecuteTransactionAction() {
        this.db = DatabaseSingleton.getDatabase();
    }

    //Setup phase
    public void setUser(User user) {
        this.user = user;
    }

    public void setTransactionDetails(Transaction transaction) {
        this.transactionDetails = transaction;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }


    //Preparation phase
    @Override
    public void prepare() throws InvalidAuthenticationException, TimeOutException, InvalidInputException {
        //Validate inputted data
        if (user == null || transactionDetails.getSender() == null || transactionDetails == null | transactionDetails.getReceiver() == null) {
            throw new InvalidInputException();
        }
        if (transactionDetails.getSender() == transactionDetails.getReceiver()) { //checks for sending money to the same account (should work for multiple accounts of different types)
            throw new InvalidInputException();
        }


        //Flag check
        // Authorize the action
        // If not authorized, this line of code will throw exception

        try {
            authorize(user, transactionDetails.getSender());
        } catch (InvalidAccountException e) {
            // The edge case, where user select an account but the account gets deleted.
            throw new InvalidInputException();
        }


        // Check if this line needs
        // TODO: delete if the exception throws correctly #MU
//        if (authorized != AUTH_STATUS.AUTHORIZED){ //checks for the state of the authorization
//            throw new IllegalStateException();
//        }

        if (transactionDetails.getAmount() <= 0) {
            throw new InsufficientFundsException();
        }
        if (transactionDetails.getSender().getBalance() < 0) {
            throw new InsufficientFundsException();
        }
        if (transactionDetails.getSender().getBalance() < transactionDetails.getAmount()) {
            throw new InsufficientFundsException();
        }
    }

    @Override
    public void execute() {
        transactionDetails.getSender().addTransaction(transactionDetails); //uncomment when needed.
        transactionDetails.getReceiver().addTransaction(transactionDetails);
        db.addTransaction(transactionDetails);
        System.out.println("[" + transactionDetails.getSender().getFullName() + " has sent $" + transactionDetails.getAmount() + " to " + transactionDetails.getReceiver().getFullName() + "] \n");
    }

    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, InvalidAccountException {
        if (user == null) {
            throw new InvalidAuthenticationException();
        }
        if (account == null) {
            throw new InvalidAccountException();
        }

        // Case: if user is a customer
        if (user instanceof Customer) {
            if (account.getCustomer().equals(user)) {
                authorized = AUTH_STATUS.AUTHORIZED;
                return;
            }
        } else {
            throw new InvalidAuthenticationException();
        }
        System.out.println("Transaction ID (" + transactionDetails.getId() + ") has been approved.");
    }
}
