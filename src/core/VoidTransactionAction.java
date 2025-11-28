package core;

import auth.core.Admin;
import auth.core.Customer;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import bank.Transaction;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;

import java.time.LocalDateTime;

/**
 * An action to void an existing transaction.
 * <p>
 * This action creates a reverse transaction and updates both sender's
 * and receiver's accounts accordingly. Only authorized users (the
 * customer who owns the account or an Admin) can void a transaction.
 */
public class VoidTransactionAction extends Action {
    /** The transaction that is to be voided. */
    private Transaction transactionToBeVoided;
    /** The user performing the void operation. */
    private User user;
    /** Database singleton for storing transactions. */
    private final DatabaseSingleton db;

    /** The reverse transaction created during the void operation. */
    private Transaction voidTransaction;
    /**
     * Gets the reverse transaction generated when voiding.
     *
     * @return the void transaction
     */
    public Transaction getVoidTransaction() {
        return voidTransaction;
    }

    /** Constructs a VoidTransactionAction and initializes the database. */
    public VoidTransactionAction() {
        this.db = DatabaseSingleton.getDatabase();
    }

    /**
     * Prepares the action by validating that the transaction exists
     * and the user is authorized to void it.
     *
     * @throws InvalidAuthenticationException if the user is not authenticated
     * @throws InvalidInputException          if the transaction does not exist
     */
    @Override
    public void prepare() throws InvalidAuthenticationException, InvalidInputException {

        if (db.getTransactionById(transactionToBeVoided.getId()) == null) {
            throw new InvalidInputException();
        }

        try {
            authorize(user, transactionToBeVoided.getSender());
        } catch (InvalidAccountException e) {
            // TODO Parameter of the exception #MU
            throw new RuntimeException(e);
        }


    }

    /**
     * Executes the void operation.
     * <p>
     * Creates a reverse transaction, updates both accounts,
     * marks the original transaction as VOIDED, and saves
     * the reverse transaction to the database.
     *
     * @throws InvalidAuthenticationException if the user is not authorized
     * @throws InvalidAccountException        if the account is invalid
     */
    @Override
    public void execute() throws InvalidAuthenticationException, InvalidAccountException {
        // Reverse of transaction to be voided
        authorize(user, transactionToBeVoided.getSender());

        Transaction voidTransaction = new Transaction(
                transactionToBeVoided.getReceiver(),
                transactionToBeVoided.getSender(),
                LocalDateTime.now(),
                transactionToBeVoided.getAmount()
        );

        transactionToBeVoided.getSender().addTransaction(voidTransaction);
        transactionToBeVoided.getReceiver().addTransaction(voidTransaction);

        transactionToBeVoided.setStatus(Transaction.TransactionStatus.VOIDED);
        // Update the database of the transaction
        db.addTransaction(voidTransaction);
        this.voidTransaction = voidTransaction;

    }

    /**
     * Authorizes the user for this action.
     * <p>
     * A customer can void a transaction only if they own the sender account.
     * Admins are always authorized.
     *
     * @param user    the user performing the action
     * @param account the account involved in the transaction
     * @throws InvalidAuthenticationException if the user is not authorized
     * @throws InvalidAccountException        if the account is invalid
     */
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
            }

        }
        // Case: if user is a admin
        else if (user instanceof Admin) {
            authorized = AUTH_STATUS.AUTHORIZED;
        } else {
            throw new InvalidAuthenticationException();
        }
        System.out.println("Transaction ID (" + transactionToBeVoided.getId() + ") has been approved to be voided.");
    }

    /**
     * Gets the transaction to be voided.
     *
     * @return the transaction
     */
    public Transaction getTransactionToBeVoided() {
        return transactionToBeVoided;
    }

    /**
     * Sets the transaction to be voided.
     *
     * @param transactionToBeVoided the transaction
     */
    public void setTransactionToBeVoided(Transaction transactionToBeVoided) {
        this.transactionToBeVoided = transactionToBeVoided;
    }

    /**
     * Gets the user performing this action.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user performing this action.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }
}
