package bank;

import auth.core.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class Account {
    protected final String accountId;
    protected Customer customer;
    protected List<Transaction> transactions;
    protected double balance;
    protected int accountStatus;

    public int ACCOUNT_STATUS_INACTIVE = 0;
    public int ACCOUNT_STATUS_ACTIVE = 1;



    public Account(Customer customer) {;
        this.accountId = UUID.randomUUID().toString();
        this.customer = customer;
        this.transactions = new ArrayList<>();
        this.accountStatus = ACCOUNT_STATUS_ACTIVE;
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
        // TODO
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }


    public double getBalance(){
        // TODO
        return 0.0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }
}

