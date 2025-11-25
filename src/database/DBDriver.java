package database;

import auth.core.*;
import bank.*;
import database.DatabaseSingleton;

import java.time.LocalDateTime;

public class DBDriver {
    public static void main(String[] args) {
        DatabaseSingleton db = DatabaseSingleton.getDatabase();
        db.printAccounts();
        db.printTransactions();
        db.printUsers();

        // Test update account
        Account card =  db.getAccountByID("9501e3ad-e672-434c-935f-c926d3a2e949");
        card.setActivity(Account.AccountStatus.INACTIVE);
        db.updateAccount(card);
    }
}
