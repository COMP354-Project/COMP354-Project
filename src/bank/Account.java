package bank;

import auth.core.Customer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class Account {
    protected final String accountId;
    protected Customer customer;
    protected List<Transaction> transactions;
    protected double balance;
    protected AccountStatus accountStatus;


    public enum AccountStatus{
        INACTIVE,
        ACTIVE;
    }

    public Account(Customer customer) {;
        this.accountId = UUID.randomUUID().toString();
        this.customer = customer;
        this.transactions = new ArrayList<>();
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public Account (String accountId, Customer customer,List<Transaction> transactions, double balance){ //Parametrized
        this.accountId = accountId;
        this.customer = customer;
        this.transactions = transactions;
        this.balance = balance;
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public String getAccountID() {
        return accountId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void voidTransaction(Transaction transaction){
        // Still needs a way to void it
        // Do we reverse the transaction?
        // TODO
        if (transaction == null || !this.transactions.contains(transaction)){
            throw new IllegalArgumentException();
        }

        if (transaction.getStatus() == Transaction.TransactionStatus.VOIDED || transaction.getStatus() == Transaction.TransactionStatus.FAILED){
            throw new IllegalStateException();
        }
        transaction.setStatus(Transaction.TransactionStatus.VOIDED); //original transaction

        Transaction reverseTransaction = new Transaction(
                transaction.getReceiver(),
                transaction.getSender(),
                LocalDateTime.now(),
                transaction.getAmount()
        );
        reverseTransaction.setStatus(Transaction.TransactionStatus.VOIDED); //reverse of the transaction

        //the logic to modify the money?

        transactions.add(reverseTransaction); //Adds it to the history
        reverseTransaction.getSender().addTransaction(reverseTransaction);
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }


    public double getBalance(){
        return balance;
    }


    public void receive(double amount){
        this.balance += amount;
    }

    public void send(double amount){
        this.balance -= amount;
    }

    public void setActivity(AccountStatus activity){
        this.accountStatus = activity;
    }

    public String getFullName(){
        return this.getCustomer().getFirstName() + " " + this.getCustomer().getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }
}

