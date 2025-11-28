package bank;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a financial transaction between two accounts.
 * <p>
 * Each transaction has a unique ID, a sender account, a receiver account,
 * a timestamp, an amount, and a status.
 * </p>
 */
public class Transaction {
    /**
     * Enum representing the possible status of a transaction.
     */
    public enum TransactionStatus {
        ACTIVE,
        VOIDED,
    }
    /** Unique identifier of the transaction. */
    private final String id;

    /** The account sending the funds. */
    private final Account sender;

    /** The account receiving the funds. */
    private final Account receiver;

    /** Timestamp of when the transaction occurred. */
    private final LocalDateTime timeOfTransaction;

    /** Amount transferred in the transaction. */
    private final double amount;

    /** Current status of the transaction. */
    private TransactionStatus status;

    /**
     * Creates a new transaction with the specified sender, receiver, timestamp, and amount.
     *
     * @param sender the account sending funds
     * @param receiver the account receiving funds
     * @param timeOfTransaction the time the transaction was made
     * @param amount the amount to transfer
     */
    public Transaction(Account sender, Account receiver, LocalDateTime timeOfTransaction, double amount) {
        this.id = UUID.randomUUID().toString();
        this.sender = sender;
        this.receiver = receiver;
        this.timeOfTransaction = timeOfTransaction;
        this.amount = amount;
        this.status = TransactionStatus.ACTIVE;
    }

    /** @return the unique transaction ID */
    public String getId() {
        return id;
    }

    /** @return the sender account */
    public Account getSender() {
        return sender;
    }

    /** @return the receiver account */
    public Account getReceiver() {
        return receiver;
    }

    /** @return the timestamp of the transaction */
    public LocalDateTime getTimeOfTransaction() {
        return timeOfTransaction;
    }

    /** @return the amount of the transaction */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the transaction status.
     *
     * @param status the new status
     */
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    /**
     * Gets the transaction amount relative to a specific account.
     * <p>
     * Returns a negative value if the account is the sender, positive if the account is the receiver.
     * </p>
     *
     * @param account the account to check
     * @return the transaction amount for that account
     */
    public double getAmountForAccount(Account account) {
        if (this.sender.equals(account)) {
            return -this.amount;
        } else {
            return this.amount;
        }
    }

    /** @return the current status of the transaction */
    public TransactionStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    public String toString() {
        return "Transaction ID: " + id +
               "\nSender Account ID: " + sender.getAccountID() +
               "\nReceiver Account ID: " + receiver.getAccountID() +
               "\nTime of Transaction: " + timeOfTransaction.toString() +
               "\nAmount: " + amount+
        "\nStatus: " + status + "\n";
    }


}
