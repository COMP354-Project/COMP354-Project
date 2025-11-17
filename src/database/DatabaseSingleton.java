package database;

import bank.*;
import com.google.gson.*;
import auth.core.*;
import com.google.gson.reflect.TypeToken;

import javax.xml.crypto.Data;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A singleton injection of the database instance
 */
public class DatabaseSingleton {
    private static DatabaseSingleton db;
    private final AccountData accountData;
//    private final UserData userData;
    private final TransactionData transactionData;
//    private final BranchData branchData;

    private DatabaseSingleton() {
//        this.userData = UserData.getUserData();
//        this.branchData = BranchData.getBranchData();
        this.accountData = AccountData.getAccountData();
        this.transactionData = TransactionData.getTransactionData();
    }

    public static DatabaseSingleton getDatabase() {
        // If database isn't instantiated, initialise it and return it
        // Else, return the initialised database
        if (db == null) {
            db = new DatabaseSingleton();
        }
        return db;
    }

    // The public APIs will be implemented below. Note that getters and setters for the
    // data handlers are not provided to avoid breaking the singleton pattern.

    /**
     * Retrieve an account by its username.
     * @param username The username of the account to be retrieved.
     * @return The Account object with the specified username, or null if not found.
     * */
    public Account getAccountByUserName(String username) {
        // Implementation to get account by username
        return this.accountData.getAccountByUserName(username);
    }

    /**
     * Retrieve an account by its ID.
     * @param id The ID of the account to be retrieved.
     * @return The Account object with the specified ID, or null if not found.
     * */
    public Account getAccountByID(String id) {
        return this.accountData.getAccountById(id);
    }

    /**
     * Retrieve a transaction by its ID.
     * @param transactionID The ID of the transaction to be retrieved.
     * @return The Transaction object with the specified ID, or null if not found.
     * */
    public Transaction getTransactionById(String transactionID) {
        return this.transactionData.getTransactionById(transactionID);
    }

    public Branch getBranchByID(String branchID) {
        return null;
    }

    public User getUserByEmail(String email) {
        return null;
    }

    /**
     * Add a new account to the database.
     * @param account The Account object to be added.
     * */
    public void addAccount(Account account) {
        boolean status = this.accountData.addAccount(account);
        if (status) {
            System.out.println("Account added successfully.");
        } else {
            System.out.println("Account already exists.");
        }
    }

    /**
     * Add a new transaction to the database.
     * @param transaction The Transaction object to be added.
     * */
    public void addTransaction(Transaction transaction) {
        boolean status = this.transactionData.addTransaction(transaction);
        if (status) {
            System.out.println("Transaction added successfully.");
        } else {
            System.out.println("Transaction already exists.");
        }
    }

    /**
     * Retrieve all transactions for a given account ID.
     * @param accountID The ID of the account whose transactions are to be retrieved.
     * @return An ArrayList of Transaction objects associated with the given account ID.
     * */
    public ArrayList<Transaction> getTransactionsByAccountID(String accountID) {
        return this.transactionData.getTransactionsByAccountId(accountID);
    }

    /**
     * Filter transactions for a given account that occurred within the specified date range.
     * @param accountID The ID of the account whose transactions are to be filtered.
     * @param start The start LocalDateTime of the date range.
     * @param end The end LocalDateTime of the date range.
     * @return An ArrayList of Transaction objects that occurred within the specified date range.
     * */
    public ArrayList<Transaction> filterTransactionsByDateRange(String accountID, LocalDateTime start, LocalDateTime end) {
        return this.transactionData.filterTransactionsByDateRange(accountID, start, end);
    }

    /**
     * Filter transactions for a given account that occurred after the specified time.
     * @param accountID The ID of the account whose transactions are to be filtered.
     * @param time The LocalDateTime after which transactions should be included.
     * @return An ArrayList of Transaction objects that occurred after the specified time.
     * */
    public ArrayList<Transaction> filterTransactionsByTime(String accountID, LocalDateTime time) {
        return this.transactionData.filterTransactionsByTime(accountID, time);
    }

    /**
     * Update an existing account in the database.
     * @param account The Account object with updated information.
     * */
    public void updateAccount(Account account) {
        boolean status = this.accountData.updateAccount(account);
        if (status) {
            System.out.println("Account updated successfully.");
        } else {
            System.out.println("Account does not exist.");
        }
    }

    public boolean updateUserInfo(User user) {
        return false;
    }

    /**
     * Print all accounts in the database.
     * */
    public void printAccounts() {
        this.accountData.printAccounts();
    }

    /**
     * Print all transactions in the database.
     * */
    public void printTransactions() {
        this.transactionData.printTransactions();
    }

    public static void main(String[] args) {
        DatabaseSingleton db = DatabaseSingleton.getDatabase();
        db.printAccounts();
        db.printTransactions();
    }
}

/**
 * AccountData class implementing FileProcessor interface
 * */
class AccountData implements FileProcessor{
    private static AccountData ad;
    private static final String PATH = "src/database/bank_data_files/account.json";
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
        // Load account data from JSON file into accounts HashMap
        FileReader fr = null;
        try {
            fr = new FileReader(PATH);
            Type a_type = new TypeToken<HashMap<String, Account>>() {}.getType();
            this.accounts = getGson().fromJson(fr, a_type);
            fr.close();
        }
        catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        // If accounts is null (e.g., file was empty), initialize it as an empty HashMap
        if (this.accounts == null) {
            this.accounts = new HashMap<>();
        }

        // Load transactions into each account
    }

    @Override
    public void save() {
        // Save accounts HashMap data into JSON file
        FileWriter fw;
        try {
            fw = new FileWriter(PATH);
            Type a_type = new TypeToken<HashMap<String, Account>>() {}.getType();
            getGson().toJson(this.accounts, a_type, fw);
            fw.close();
        }
        catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Account.class, new AccountAdapter())
                .create();
    }

    public void printAccounts() {
        for (String key : this.accounts.keySet()) {
            System.out.println(accounts.get(key));
        }
    }

    public Account getAccountById(String accountID) {
        return this.accounts.get(accountID);
    }

    public Account getAccountByUserName(String username) {
        for (Account account : this.accounts.values()) {
            String first = account.getCustomer().getFirstName();
            String last = account.getCustomer().getLastName();
            String fullName = first + " " + last;
            if (fullName.equals(username)) {
                return account;
            }
        }
        return null;
    }

    public boolean addAccount(Account account) {
        if (this.accounts.containsKey(account.getAccountID())) {
            return false; // Account already exists
        }
        this.accounts.put(account.getAccountID(), account);
        this.save(); // Save changes to file
        return true;
    }

    public boolean updateAccount(Account account) {
        if (!this.accounts.containsKey(account.getAccountID())) {
            return false; // Account does not exist
        }
        this.accounts.put(account.getAccountID(), account);
        this.save(); // Save changes to file
        return true;
    }
}

/**
 * UserData class implementing FileProcessor interface.
 * This class loads and manages user data where users are Customer, Teller, or Admin.
 * */
class UserData implements FileProcessor{
    private static UserData rd;
    private static final String PATH = "src/database/bank_data_files/user.json";
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

    @Override
    public Gson getGson() {
        return null;
    }
}

/**
 * TransactionData class implementing FileProcessor interface
 * */
class TransactionData implements FileProcessor {
    private static TransactionData td;
    private static final String PATH = "src/database/bank_data_files/transaction.json";
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
        // Load account data from JSON file into accounts HashMap
        FileReader fr = null;
        try {
            fr = new FileReader(PATH);
            Type t_type = new TypeToken<HashMap<String, Transaction>>() {
            }.getType();
            this.transactions = getGson().fromJson(fr, t_type);
            fr.close();
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        // If accounts is null (e.g., file was empty), initialize it as an empty HashMap
        if (this.transactions == null) {
            this.transactions = new HashMap<>();
        }
    }

    @Override
    public void save() {
        // Save accounts HashMap data into JSON file
        FileWriter fw;
        try {
            fw = new FileWriter(PATH);
            Type t_type = new TypeToken<HashMap<String, Transaction>>() {
            }.getType();
            getGson().toJson(this.transactions, t_type, fw);
            fw.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Account.class, new AccountAdapter())
                .setPrettyPrinting()
                .create();
    }

    public void printTransactions() {
        for (String key : this.transactions.keySet()) {
            System.out.println(transactions.get(key));
        }
    }

    public Transaction getTransactionById(String transactionID) {
        return this.transactions.get(transactionID);
    }

    public boolean addTransaction(Transaction transaction) {
        if (this.transactions.containsKey(transaction.getId())) {
            return false; // Transaction already exists
        }
        this.transactions.put(transaction.getId(), transaction);
        this.save(); // Save changes to file
        return true;
    }

    public ArrayList<Transaction> getTransactionsByAccountId(String accountID) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction transaction : this.transactions.values()) {
            if (transaction.getSender().getAccountID().equals(accountID) ||
                transaction.getReceiver().getAccountID().equals(accountID)) {
                result.add(transaction);
            }
        }
        return result;
    }

    public ArrayList<Transaction> filterTransactionsByDateRange(String accountID, LocalDateTime start, LocalDateTime end) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction transaction : this.transactions.values()) {
            if ((transaction.getSender().getAccountID().equals(accountID) ||
                 transaction.getReceiver().getAccountID().equals(accountID)) &&
                (transaction.getTimeOfTransaction().isAfter(start) || transaction.getTimeOfTransaction().isEqual(start)) &&
                (transaction.getTimeOfTransaction().isBefore(end) || transaction.getTimeOfTransaction().isEqual(end))) {
                result.add(transaction);
            }
        }
        return result;
    }

    public ArrayList<Transaction> filterTransactionsByTime(String accountID, LocalDateTime time) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction transaction : this.transactions.values()) {
            if ((transaction.getSender().getAccountID().equals(accountID) ||
                    transaction.getReceiver().getAccountID().equals(accountID)) &&
                    transaction.getTimeOfTransaction().isAfter(time)) {
                result.add(transaction);
            }
        }
        return result;
    }
}

/**
 * BranchData class implementing FileProcessor interface
 * */
class BranchData implements FileProcessor{
    private static BranchData bd;
    private static final String PATH = "src/database/bank_data_files/branch.json";
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

    @Override
    public Gson getGson() {
        return null;
    }
}
