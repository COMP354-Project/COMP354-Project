package core;

import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import bank.Transaction;
import core.exceptions.InvalidAccountException;
import database.DatabaseSingleton;
import org.junit.jupiter.api.Assertions;
import java.time.LocalDateTime;

class TransactionActionsTest {
    @org.junit.jupiter.api.Test
    void executeTransaction() {
        // Setup database
        DatabaseSingleton db = DatabaseSingleton.getDatabase();
        // Setup transactions and accounts
        Account sourceAccount = db.getAccountByID("30f49d9c-0b0b-49bd-a511-84d2e648daf5");
        Account destinationAccount = db.getAccountByID("e44ed2b1-e5f2-489b-8beb-98ff3c7d2706");
        Transaction testedTransaction = new Transaction(sourceAccount, destinationAccount, LocalDateTime.now(), 20);
        User user = sourceAccount.getCustomer();

        // Log - Testing parameters
        System.out.println("==========[Testing parameters - START]==========");
        System.out.println(sourceAccount);
        System.out.println(destinationAccount);
        double sourceAccountBalance = sourceAccount.getBalance();
        double destinationAccountBalance = destinationAccount.getBalance();
        System.out.println("==========[Testing parameters - END]==========\n");

        // Setup transaction
        ExecuteTransactionAction sendAction = new ExecuteTransactionAction();
        sendAction.setUser(user);
        sendAction.setTransactionDetails(testedTransaction);
        try {
            sendAction.execute();
        } catch (InvalidAccountException | InvalidAuthenticationException e) {
            System.err.println(e.getMessage());
        }

        // Log - Testing results
        System.out.println("==========[Testing results - START]==========");
        System.out.println(sourceAccount);
        System.out.println(destinationAccount);
        System.out.println("==========[Testing results - END]==========\n");

        // Soft assertion, don't throw exceptions
        Assertions.assertAll(
                // Test case - Source account successfully updated
                () -> Assertions.assertEquals(sourceAccount.getBalance(), sourceAccountBalance - testedTransaction.getAmount()),
                // Test case - Destination account successfully updated
                () -> Assertions.assertEquals(destinationAccount.getBalance(), destinationAccountBalance + testedTransaction.getAmount()),
                () -> {
                    // Revert test transactions from buffer and database
                    sourceAccount.getTransactions().remove(testedTransaction);
                    destinationAccount.getTransactions().remove(testedTransaction);
                    db.revertTransaction(testedTransaction);
                }
        );
    }

    @org.junit.jupiter.api.Test
    void voidTransaction() {
        // Setup database
        DatabaseSingleton db = DatabaseSingleton.getDatabase();
        // Setup transactions and accounts to be voided
        Account sourceAccount = db.getAccountByID("30f49d9c-0b0b-49bd-a511-84d2e648daf5");
        Account destinationAccount = db.getAccountByID("e44ed2b1-e5f2-489b-8beb-98ff3c7d2706");
        Transaction testedTransaction = new Transaction(sourceAccount, destinationAccount, LocalDateTime.now(), 20);
        User user = sourceAccount.getCustomer();

        ExecuteTransactionAction sendAction = new ExecuteTransactionAction();
        sendAction.setUser(user);
        sendAction.setTransactionDetails(testedTransaction);
        try {
            sendAction.execute();
        } catch (InvalidAccountException | InvalidAuthenticationException e) {
            System.err.println(e.getMessage());
        }

        // Log - Testing parameters
        System.out.println("==========[Testing parameters - START]==========");
        System.out.println(sourceAccount);
        System.out.println(destinationAccount);
        double sourceAccountBalance = sourceAccount.getBalance();
        double destinationAccountBalance = destinationAccount.getBalance();
        System.out.println("==========[Testing parameters - END]==========\n");

        VoidTransactionAction voidAction = new VoidTransactionAction();
        voidAction.setUser(user);
        voidAction.setTransactionToBeVoided(testedTransaction);
        try {
            voidAction.execute();

        } catch (InvalidAuthenticationException | InvalidAccountException e) {
            throw new RuntimeException(e);
        }

        // Log - Testing results
        System.out.println("==========[Testing results - START]==========");
        System.out.println(sourceAccount);
        System.out.println(destinationAccount);
        System.out.println("==========[Testing results - END]==========\n");


        Assertions.assertAll(
                // Test case - Source account successfully updated
                () -> Assertions.assertEquals(sourceAccount.getBalance(), sourceAccountBalance + testedTransaction.getAmount()),
                // Test case - Destination account successfully updated
                () -> Assertions.assertEquals(destinationAccount.getBalance(), destinationAccountBalance - testedTransaction.getAmount()),
                () -> {
                    // Revert test transactions from buffer and database
                    sourceAccount.getTransactions().remove(testedTransaction);
                    destinationAccount.getTransactions().remove(testedTransaction);
                    db.revertTransaction(testedTransaction);

                    sourceAccount.getTransactions().remove(voidAction.getVoidTransaction());
                    destinationAccount.getTransactions().remove(voidAction.getVoidTransaction());
                    db.revertTransaction(voidAction.getVoidTransaction());
                }
        );
    }

    @org.junit.jupiter.api.Test
    void authorize() {
    }


}