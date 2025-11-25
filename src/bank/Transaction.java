package bank;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaction {
    public enum TransactionStatus {
        ACTIVE,
        VOIDED,
    }
    //test
    private final String id;
    private final Account sender;
    private final Account receiver;
    private final LocalDateTime timeOfTransaction;
    private final double amount;
    private TransactionStatus status;

    public Transaction(Account sender, Account receiver, LocalDateTime timeOfTransaction, double amount) {
        this.id = UUID.randomUUID().toString();
        this.sender = sender;
        this.receiver = receiver;
        this.timeOfTransaction = timeOfTransaction;
        this.amount = amount;
        this.status = TransactionStatus.ACTIVE;
    }

    public String getId() {
        return id;
    }

    public Account getSender() {
        return sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public LocalDateTime getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public double getAmount() {
        return amount;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

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
