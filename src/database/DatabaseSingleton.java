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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import database.LocalDateTimeAdapter;
import database.TransactionAdapter;
import database.AccountAdapter;

/**
 * A singleton injection of the database instance
 */
public class DatabaseSingleton {
    private static DatabaseSingleton db;
//    private AccountData accountData;
//    private UserData userData;
//    private TransactionData transactionData;
//    private BranchData branchData;

    private DatabaseSingleton() {
//        this.userData = UserData.getUserData();
//        this.branchData = BranchData.getBranchData();
//        this.accountData = AccountData.getAccountData();
//        this.transactionData = TransactionData.getTransactionData();
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

    public static void main(String[] args) {
        Customer cust1 = new Customer("abc@gmail.com", "password", "John", "Doe");
        Customer cust2 = new Customer("def@gmail.com", "password", "John", "Joe");

        Account acc1 = new Chequing(cust1);
        Account acc2 = new Chequing(cust2);

        Transaction tx1 = new Transaction(acc1, acc2, LocalDateTime.now(), 100.0);

        acc1.addTransaction(tx1);
        acc2.addTransaction(tx1);

        HashMap<String, Account> accounts = new HashMap<>();
        accounts.put(acc1.getAccountID(), acc1);
        accounts.put(acc2.getAccountID(), acc2);
        // print JSON string representation of the account
        Gson accountGson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Transaction.class, new TransactionAdapter())
                .setPrettyPrinting()
                .create();
        String ajson = accountGson.toJson(accounts);
        System.out.println(ajson);

        Gson transactionGson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Account.class, new AccountAdapter())
                .setPrettyPrinting()
                .create();
        String tjson = transactionGson.toJson(tx1);
        System.out.println(tjson);


    }
}

/**
 * AccountData class implementing FileProcessor interface
 * */
class AccountData implements FileProcessor{
    private static AccountData ad;
    private HashMap<String, Account> accounts;

    private AccountData() {
        this.load();
    }

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

/**
 * UserData class implementing FileProcessor interface.
 * This class loads and manages user data where users are Customer, Teller, or Admin.
 * */
class UserData implements FileProcessor{
    private static UserData rd;
    private HashMap<String, User> users;

    private UserData() {
        this.load();
    }

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

/**
 * TransactionData class implementing FileProcessor interface
 * */
class TransactionData implements FileProcessor{
    private static TransactionData td;
    private HashMap<String, Transaction> transactions;

    private TransactionData() {
        this.load();
    }

    public static TransactionData getTransactionData() {
        if (td == null) {
            td = new TransactionData();
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

/**
 * UserData class implementing FileProcessor interface
 * */
class BranchData implements FileProcessor{
    private static BranchData bd;
    private HashMap<String, Branch> branches;

    private BranchData() {
        this.load();
    }

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
