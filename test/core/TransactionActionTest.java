package core;

import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.Account;
import bank.Transaction;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;

import java.time.LocalDateTime;

class TransactionActionTest {


    @org.junit.jupiter.api.Test
    void prepare() {

        DatabaseSingleton db = DatabaseSingleton.getDatabase();
        // Database Setup
        Account sourceAccount = db.getAccountByID("30f49d9c-0b0b-49bd-a511-84d2e648daf5");
        Account destinationAccount = db.getAccountByID("e44ed2b1-e5f2-489b-8beb-98ff3c7d2706");
        User user = sourceAccount.getCustomer();

        System.out.println(sourceAccount);
        System.out.println(destinationAccount);

        // Setup transaction
        TransactionAction action = new TransactionAction();
        action.setUser(user);
        action.setSourceAccount(sourceAccount);
        action.setDestinationAccount(destinationAccount);
        action.setTransactionDetails(new Transaction(sourceAccount, destinationAccount, LocalDateTime.now(), 20));

        try {
            action.prepare();
        } catch (InvalidAuthenticationException e) {
            throw new RuntimeException(e);
        } catch (InvalidInputException e) {
            throw new RuntimeException(e);
        }

        /*

        "30f49d9c-0b0b-49bd-a511-84d2e648daf5": {
    "accountId": "30f49d9c-0b0b-49bd-a511-84d2e648daf5",
    "customer": {
      "firstName": "Joseph",
      "lastName": "Joestar",
      "email": "abc@gmail.com",
      "password": "password"
    },
    "balance": 100.0,
    "accountStatus": "ACTIVE",
    "type": "Chequing"
  }

        */




        /*
        "e44ed2b1-e5f2-489b-8beb-98ff3c7d2706": {
    "accountId": "e44ed2b1-e5f2-489b-8beb-98ff3c7d2706",
    "customer": {
      "firstName": "Jimmy",
      "lastName": "Neutron",
      "email": "jjj@gmail.com",
      "password": "password"
    },
    "balance": 20.0,
    "accountStatus": "ACTIVE",
    "type": "Saving"
  }

         */


    }

    void execute() {
    }

    @org.junit.jupiter.api.Test
    void authorize() {
    }

    @org.junit.jupiter.api.Test
    void wasExecuted() {
    }

}