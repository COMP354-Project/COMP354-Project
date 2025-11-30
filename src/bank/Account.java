package bank;

import auth.core.Customer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a bank account belonging to a customer.
 * Contains information about the account ID, associated customer,
 * transactions, and the account's status.
 * <p>
 * Subclasses should implement specific account types (e.g., Chequing, Saving, Card).
 * </p>
 */
public abstract class Account {
    /** Unique identifier for the account. */
    protected String accountId;
    /** Customer who owns this account. */
    protected Customer customer;
    /** List of transactions associated with this account. */
    protected List<Transaction> transactions;
    /** Current status of the account (ACTIVE or INACTIVE). */
    protected AccountStatus accountStatus;
    /**
     * Enum representing the account's activity status.
     */
    public enum AccountStatus {
        /** The account is disabled or not allowed to perform transactions. */
        INACTIVE,
        /** The account is fully functional and allowed to perform transactions. */
        ACTIVE;
    }
    /**
     * Creates a new account with a randomly generated ID and initializes
     * the transaction list. Sets account status to ACTIVE by default.
     *
     * @param customer the customer who owns the account
     */
    public Account(Customer customer) {
        ;
        this.accountId = UUID.randomUUID().toString();
        this.customer = customer;
        this.transactions = new ArrayList<>();
        this.accountStatus = AccountStatus.ACTIVE;
    }
    /**
     * Creates a new account with a given ID, customer, and transaction history.
     *
     * @param accountId    the account ID
     * @param customer     the customer who owns the account
     * @param transaction  initial list of transactions
     */
    public Account(String accountId, Customer customer, List<Transaction> transaction) { //Parametrized
        this.accountId = accountId;
        this.customer = customer;
        this.transactions = transaction;
        this.accountStatus = AccountStatus.ACTIVE;
    }
    /**
     * Updates this account's details using another account's data.
     *
     * @param account the account to copy details from
     */
    public void update(Account account) {
        this.customer = account.getCustomer();
        this.transactions = account.getTransactions();
        this.accountStatus = account.getAccountStatus();
    }
    /**
     * Sets the account ID.
     *
     * @param id the new account ID
     */
    public void setAccountId(String id) {
        this.accountId = id;
    }
    /**
     * Returns the account ID.
     *
     * @return account ID
     */
    public String getAccountID() {
        return accountId;
    }
    /**
     * Returns the customer associated with this account.
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }
    /**
     * Sets the customer for this account.
     *
     * @param customer the customer to associate
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    /**
     * Adds a transaction to this account's transaction history.
     *
     * @param transaction the transaction to add
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

//    public void voidTransaction(Transaction transaction) {
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
//    }
    /**
     * Returns the list of transactions for this account.
     *
     * @return list of transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Returns the current balance of the account.
     * Calculates by summing incoming transactions and subtracting outgoing transactions.
     *
     * @return the account balance
     */
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
    /**
     * Sets the activity status of this account.
     *
     * @param activity new activity status
     */
    public void setActivity(AccountStatus activity) {
        this.accountStatus = activity;
    }
    /**
     * Returns the full name of the account owner.
     *
     * @return full name of the customer
     */
    public String getFullName() {
        return this.getCustomer().getFirstName() + " " + this.getCustomer().getLastName();
    }
    /**
     * Returns the current status of this account.
     *
     * @return account status
     */
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

