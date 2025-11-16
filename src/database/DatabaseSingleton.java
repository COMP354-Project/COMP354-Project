package database;

import database.FileProcessor;
import com.google.gson.*;
import bank.Account;
import bank.Branch;
import bank.Card;
import bank.Chequing;
import bank.Saving;
import bank.Transaction;
import auth.core.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A singleton injection of the database instance
 */
public class DatabaseSingleton {
    private static DatabaseSingleton db;
    private AccountData accountData;
    private UserData userData;
    private TransitionData transitionData;
    private BranchData branchData;

    private DatabaseSingleton() {
        this.accountData = AccountData.getAccountData();
        this.userData = UserData.getUserData();
        this.transitionData = TransitionData.getTransitionData();
        this.branchData = BranchData.getBranchData();
    }

    public static DatabaseSingleton getDataBase() {
        // If database isn't instantiated, initialise it and return it
        // Else, return the initialised database
        if (db == null) {
            db = new DatabaseSingleton();
        }
        return db;
    }

    // The public APIs will be implemented below. Note that getters and setters for the
    // data handlers are not provided to avoid breaking the singleton pattern.

    public Account getAccountByUserName(String username) {
        // Implementation to get account by username
        return null;
    }

    public Account getAccountByUID(String uid) {
        return null;
    }

    public Transaction getTransactionByID(String transactionID) {
        return null;
    }

    public Branch getBranchByID(String branchID) {
        return null;
    }

    public User getUserByEmail(String email) {
        return null;
    }

    public void addAccount(Account account) {
        System.out.println("Adding account...");
    }

    public void addTransaction(Transaction transaction) {
        System.out.println("Adding transaction...");
    }

    public ArrayList<Transaction> getTransactionsByAccountID(String accountID) {
        return null;
    }

    public ArrayList<Transaction> filterTransactionsByDateRange(String accountID, LocalDateTime start, LocalDateTime end) {
        return null;
    }

    public ArrayList<Transaction> filterTransactionsByTime(String accountID, LocalDateTime time) {
        return null;
    }

    public boolean updateAccount(Account account) {
        return false;
    }

    public boolean updateUserInfo(User user) {
        return false;
    }
}

class AccountData implements FileProcessor{
    private static AccountData ad;

    private AccountData() {}

    public static AccountData getAccountData() {
        if (ad == null) {
            ad = new AccountData();
        }
        return ad;
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }
}

class UserData implements FileProcessor{
    private static UserData rd;

    private UserData() {}

    public static UserData getUserData() {
        if (rd == null) {
            rd = new UserData();
        }
        return rd;
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }
}

class TransitionData implements FileProcessor{
    private static TransitionData td;
    private TransitionData() {}
    public static TransitionData getTransitionData() {
        if (td == null) {
            td = new TransitionData();
        }
        return td;
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }
}

class BranchData implements FileProcessor{
    private static BranchData bd;
    private BranchData() {}
    public static BranchData getBranchData() {
        if (bd == null) {
            bd = new BranchData();
        }
        return bd;
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }
}
