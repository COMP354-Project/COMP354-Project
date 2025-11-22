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

public class VoidTransactionAction extends Action {

    private Transaction transactionToBeVoided;
    private User user;
    private final DatabaseSingleton db;

    private Transaction voidTransaction;
    public Transaction getVoidTransaction() {
        return voidTransaction;
    }


    public VoidTransactionAction() {
        this.db = DatabaseSingleton.getDatabase();
    }

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

    public Transaction getTransactionToBeVoided() {
        return transactionToBeVoided;
    }

    public void setTransactionToBeVoided(Transaction transactionToBeVoided) {
        this.transactionToBeVoided = transactionToBeVoided;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
