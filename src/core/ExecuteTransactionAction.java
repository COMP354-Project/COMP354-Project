package core;

import auth.core.Customer;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import auth.exceptions.LackOfClearanceException;
import core.exceptions.InsufficientFundsException;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import auth.exceptions.TimeOutException;
import bank.Account;
import bank.Branch;
import bank.Transaction;
import database.DatabaseSingleton;

/**
 * Action to execute a monetary transaction between two accounts.
 * <p>
 * Only authorized users can perform transactions. Customers can only send
 * from their own accounts. Transactions are validated for sufficient funds,
 * valid accounts, and non-zero positive amounts.
 */
public class ExecuteTransactionAction extends Action {
    /** Database instance. */
    DatabaseSingleton db;
    /** The user initiating the transaction. */
    private User user;
    /** The details of the transaction to be executed. */
    private Transaction transactionDetails;
    /** The branch associated with the transaction (currently unused). */
    private Branch branch; //use to check for fraud? not used for anything for now

    /**
     * Constructs a new transaction action and initializes the database reference.
     */
    public ExecuteTransactionAction() {
        this.db = DatabaseSingleton.getDatabase();
        this.authorized = AUTH_STATUS.NOT_AUTHORIZED;
    }

    /**
     * Sets the user performing the transaction.
     *
     * @param user the user initiating the transaction
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Sets the transaction details.
     *
     * @param transaction the transaction to execute
     */
    public void setTransactionDetails(Transaction transaction) {
        this.transactionDetails = transaction;
    }

    /**
     * Sets the branch associated with the transaction.
     *
     * @param branch the branch
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    /**
     * Validates the transaction before execution.
     *
     * @throws InvalidAuthenticationException if the user is null
     * @throws InvalidInputException if sender, receiver, or amount is invalid
     * @throws TimeOutException currently unused, placeholder for timeout logic
     * @throws InsufficientFundsException if sender's balance is negative
     */
    @Override
    public void prepare() throws InvalidAuthenticationException, TimeOutException, InvalidInputException, InsufficientFundsException {
        //Validate inputted data
        if (user == null || transactionDetails.getSender() == null || transactionDetails == null | transactionDetails.getReceiver() == null) {
            throw new InvalidInputException();
        }
        if (transactionDetails.getSender() == transactionDetails.getReceiver()) { //checks for sending money to the same account (should work for multiple accounts of different types)
            throw new InvalidInputException();
        }
        if (transactionDetails.getAmount() <= 0) {
            throw new InvalidInputException();
        }
    }

    /**
     * Executes the transaction after validation and authorization.
     *
     * @throws InvalidAuthenticationException if the user is not authorized
     * @throws InvalidAccountException if either account no longer exists
     */
    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        if (transactionDetails.getSender().getBalance() < 0) {
            throw new InsufficientFundsException();
        }
        if (transactionDetails.getSender().getBalance() < transactionDetails.getAmount()) {
            throw new InsufficientFundsException();
        }
        //Flag check
        // Authorize the action
        // If not authorized, this line of code will throw exception
        if (!isAuthorized()) {
            try {
                authorize(user, transactionDetails.getSender());
            } catch (InvalidAccountException e) {
                // The edge case, where user select an account but the account gets deleted.
                throw new InvalidAccountException();
            } catch (LackOfClearanceException e) {
                throw new LackOfClearanceException();
            }
        }

        transactionDetails.getSender().addTransaction(transactionDetails); //uncomment when needed.
        transactionDetails.getReceiver().addTransaction(transactionDetails);
        db.addTransaction(transactionDetails);
        System.out.println("[" + transactionDetails.getSender().getFullName() + " has sent $" + transactionDetails.getAmount() + " to " + transactionDetails.getReceiver().getFullName() + "] \n");
    }

    /**
     * Authorizes the user to perform the transaction.
     *
     * @param user the user performing the transaction
     * @param account the sender account
     * @throws InvalidAuthenticationException if the user is not a Customer
     * @throws LackOfClearanceException if the user is a Customer but does not own the sender account
     * @throws InvalidAccountException if the account is null
     */
    @Override
    public void authorize(User user, Account account) throws InvalidAuthenticationException, LackOfClearanceException, InvalidAccountException {
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
            } else if (transactionDetails.getSender().getAccountID().equals("8df41236-c149-4421-83e8-07a4e4618498")) {
                // Hard-coded, letting all transactions with ATM as sender, be authorized.
                authorized = AUTH_STATUS.AUTHORIZED;
            } else {
                throw new LackOfClearanceException();
            }
        } else {
            throw new InvalidAuthenticationException();
        }
        System.out.println("Transaction ID (" + transactionDetails.getId() + ") has been approved.");
    }
}
