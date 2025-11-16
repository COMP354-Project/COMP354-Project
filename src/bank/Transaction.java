package bank;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaction {
    //test
    private final String id;
    private final Account sender;
    private final Account receiver;
    private final LocalDateTime timeOfTransaction;
    private final double amount;

    public Transaction(Account sender, Account receiver, LocalDateTime timeOfTransaction, double amount) {
        this.id = UUID.randomUUID().toString();
        this.sender = sender;
        this.receiver = receiver;
        this.timeOfTransaction = timeOfTransaction;
        this.amount = amount;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }
}
