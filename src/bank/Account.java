package bank;

import auth.core.Customer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class Account {
    protected String accountId;
    protected Customer customer;
    protected List<Transaction> transactions;
    protected AccountStatus accountStatus;

    public enum AccountStatus {
        INACTIVE,
        ACTIVE;
    }

    public Account(Customer customer) {
        ;
        this.accountId = UUID.randomUUID().toString();
        this.customer = customer;
        this.transactions = new ArrayList<>();
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public Account(String accountId, Customer customer, List<Transaction> transaction) { //Parametrized
        this.accountId = accountId;
        this.customer = customer;
        this.transactions = transaction;
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public void update(Account account) {
        this.customer = account.getCustomer();
        this.transactions = account.getTransactions();
        this.accountStatus = account.getAccountStatus();
    }

    public void setAccountId(String id) {
        this.accountId = id;
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

    public void voidTransaction(Transaction transaction) {
//        // Still needs a way to void it
//        // Do we reverse the transaction?
//        // TODO
//        if (transaction == null || !this.transactions.contains(transaction)) {
//            throw new IllegalArgumentException();
//        }
//
//        if (transaction.getStatus() == Transaction.TransactionStatus.VOIDED || transaction.getStatus() == Transaction.TransactionStatus.FAILED) {
//            throw new IllegalStateException();
//        }
//        transaction.setStatus(Transaction.TransactionStatus.VOIDED); //original transaction
//
//        Transaction reverseTransaction = new Transaction(
//                transaction.getReceiver(),
//                transaction.getSender(),
//                LocalDateTime.now(),
//                transaction.getAmount()
//        );
//        reverseTransaction.setStatus(Transaction.TransactionStatus.VOIDED); //reverse of the transaction
//
//        //the logic to modify the money?
//
//        transactions.add(reverseTransaction); //Adds it to the history
//        reverseTransaction.getSender().addTransaction(reverseTransaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }


    public double getBalance() {
        double balance = 0;
        for (Transaction t : this.transactions) {
            if (this.equals(t.getReceiver()))
                balance += t.getAmount();
            else {
                balance -= t.getAmount();
            }
        }
        return balance;
    }

    public void setActivity(AccountStatus activity) {
        this.accountStatus = activity;
    }

    public String getFullName() {
        return this.getCustomer().getFirstName() + " " + this.getCustomer().getLastName();
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public String toString() {
        String res = "Account ID: " + accountId + "\n" +
                "Account Type: " + this.getClass().getSimpleName() + "\n" +
                "Customer: " + customer.getFirstName() + " " + customer.getLastName() + "\n"
                + "Transactions: \n";
        if (transactions.isEmpty()) {
            res += "- No transactions\n";
        }
        for (Transaction t : transactions) {
            res += "- " + t.toString() + "\n";
        }
        res += "Balance: " + getBalance() + "\n" +
                "Account Status: " + accountStatus + "\n";
        return res;
    }
}

