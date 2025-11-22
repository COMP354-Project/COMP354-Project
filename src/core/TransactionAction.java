package core;

import auth.core.Customer;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import core.exceptions.InsufficientFundsException;
import core.exceptions.InvalidInputException;
import auth.exceptions.TimeOutException;
import bank.Account;
import bank.Branch;
import bank.Transaction;

public class TransactionAction extends Action {
    //Data needed to prepare
    private User user;
    private Account sourceAccount;
    private Account destinationAccount; //Optional for destination
    private Transaction transactionDetails;
    private Branch branch; //use to check for fraud? not used for anything for now


    //Flags
    private boolean prepared;
    private boolean executed;

    //Setup phase
    public void setUser(User user) {
        this.user = user;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
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
        if (user == null || sourceAccount == null || transactionDetails == null | destinationAccount == null) {
            throw new InvalidInputException();
        }
        if (sourceAccount == destinationAccount) { //checks for sending money to the same account (should work for multiple accounts of different types)
            throw new InvalidInputException();
        }
        //Flag check
        // Authorize the action
        // If not authorized, this line of code will throw exception
        authorize(user, sourceAccount);

        // Check if this line needs
        // TODO: delete if the exception throws correctly
//        if (authorized != AUTH_STATUS.AUTHORIZED){ //checks for the state of the authorization
//            throw new IllegalStateException();
//        }

        if (transactionDetails.getAmount() <= 0) {
            throw new InsufficientFundsException();
        }
        if (sourceAccount.getBalance() < 0) {
            throw new InsufficientFundsException();
        }
        if (sourceAccount.getBalance() < transactionDetails.getAmount()) {
            throw new InsufficientFundsException();
        }
        prepared = true;
    }

    @Override
    public void execute() {
        if (!prepared) {
            throw new IllegalStateException();
        }

        double amount = transactionDetails.getAmount();

        //sourceAccount.addTransaction(transactionDetails); //uncomment when needed.
        destinationAccount.addTransaction(transactionDetails);

        //Not sure how money is actually managed for now, can be removed later
        sourceAccount.send(amount);
        destinationAccount.receive(amount);

        System.out.println(sourceAccount.getFullName() + " has sented" + transactionDetails.getAmount() + "$ to " + destinationAccount.getFullName() + ".");

        transactionDetails.setStatus(Transaction.TransactionStatus.EXECUTED);

        //Do the transfers?

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
                return;
            }
        } else {
            throw new InvalidAuthenticationException();
        }
        System.out.println("Transaction ID (" + transactionDetails.getId() + ") has been approved.");
    }


    public boolean wasExecuted() {
        return executed;
    }
}
