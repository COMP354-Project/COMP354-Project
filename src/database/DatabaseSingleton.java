package database;

import bank.*;
import com.google.gson.*;
import auth.core.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import database.exceptions.*;

/**
 * <p><em>DatabaseSingleton</em> provides public methods to manipulate and retrieve data on the program's <em>Account, Transaction, Branch & User</em> objects.</p>
 * <p>It uses the Singleton design pattern to ensure that only one instance of the database exists throughout the application lifecycle.</p>
 * <p>The class encapsulates data handlers for each object type, which are responsible for loading and saving data to JSON files.</p>
 * <p>Public APIs are provided to interact with the data, such as retrieving accounts by username or ID, adding new accounts or transactions, and filtering transactions by date range or time.</p
 * <p>This design ensures centralized data management and consistency across the application.</p>
 * <p>The subcomponents of DatabaseSingleton includes:</p>
 * <ul>
 *     <li><em>AccountData</em>: manages Account database by updating, adding, retrieving, etc.</li>
 *     <li><em>TransactionData</em>: manages Transaction database, provides operations such as adding, filtering and retrieving.</li>
 *     <li><em>UserData</em>: Manages user data (Customer, Teller, Admin), including loading from and saving to JSON files.</li>
 *     <li><em>BranchData</em>: Manages branch data, including loading from and saving to JSON files.</li>
 * </ul>
 *
 * @author Cong Minh Le (40264100)
 * */
public class DatabaseSingleton {
    private static DatabaseSingleton db;
    private final AccountData accountData;
    private final UserData userData;
    private final TransactionData transactionData;
//    private final BranchData branchData;

    private DatabaseSingleton() {
        this.userData = UserData.getUserData();
//        this.branchData = BranchData.getBranchData();
        this.accountData = AccountData.getAccountData();
        this.transactionData = TransactionData.getTransactionData();

        //

        // Load transactions into each account. This is only done once upon initialisation of the database.
        // Hence, no method is needed.
        for (Transaction transaction : this.transactionData.transactions.values()) {
            String senderID = transaction.getSender().getAccountID();
            String receiverID = transaction.getReceiver().getAccountID();
            try {
                Account senderAccount = this.accountData.getAccountById(senderID);
                senderAccount.addTransaction(transaction);
                Account receiverAccount = this.accountData.getAccountById(receiverID);
                receiverAccount.addTransaction(transaction);
            } catch (InvalidAccountIDException e) {
                System.out.println(e.getMessage());
            }
        }
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
     * Retrieve account(s) by their username. Since a user can have multiple accounts, this method returns a list of accounts.
     * @param username The username of the account to be retrieved.
     * @return The ArrayList of Account objects with the specified username, or null if not found.
     * */
    public ArrayList<Account> getAccountsByUserName(String username) {
        // Implementation to get account by username
        ArrayList<Account> res = null;
        try {
            res = this.accountData.getAccountsByUserName(username);
            System.out.println(res.size() + " accounts with username " + username + " retrieved successfully.");
        }
        catch (InvalidUsernameException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    /**
     * Retrieve account(s) by their email. Since a user can have multiple accounts, this method returns a list of accounts.
     * @param email The email of the account to be retrieved.
     * @return The ArrayList of Account objects with the specified email, or null if not found.
     * */
    public ArrayList<Account> getAccountsByEmail(String email) {
        // Implementation to get account by username
        ArrayList<Account> res = null;
        try {
            res = this.accountData.getAccountsByEmail(email);
            System.out.println(res.size() + " accounts with email " + email + " retrieved successfully.");
        }
        catch (InvalidEmailException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    /**
     * Retrieve an account by its ID.
     * @param id The ID of the account to be retrieved.
     * @return The Account object with the specified ID, or null if not found.
     * */
    public Account getAccountByID(String id) {
        Account res = null;
        try {
            res = this.accountData.getAccountById(id);
            System.out.println("Account with ID " + id + " retrieved successfully.");
        } catch (InvalidAccountIDException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    /**
     * Retrieve a transaction by its ID.
     * @param transactionID The ID of the transaction to be retrieved.
     * @return The Transaction object with the specified ID, or null if not found.
     * */
    public Transaction getTransactionById(String transactionID) {
        Transaction res = null;
        try {
            res = this.transactionData.getTransactionById(transactionID);
            System.out.println("Transaction with ID " + transactionID + " retrieved successfully.");
        }
        catch (TransactionNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public Branch getBranchByID(String branchID) {
        return null;
    }

    public User getUserByEmail(String email) {
        return this.userData.getUserByEmail(email);
    }

    public Account getAccountByUser(User user){
        return this.accountData.getAccountByEmail(user.getEmail());
    }

    /**
     * Add a new user to the database. This method should be invoked every time an Action related to creating a user is performed.
     * @param user The User object to be added.
     * */
    public void addUser(User user) {
        try {
            this.userData.addUser(user);
            System.out.println("User added to database successfully.");
        } catch (UserAlreadyExistedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Add a new account to the database. This method should be invoked every time an Action related to creating an account is performed.
     * @param account The Account object to be added.
     * */
    public void addAccount(Account account) {
        try {
            this.accountData.addAccount(account);
            System.out.println("Account added to database successfully.");
        } catch (AccountAlreadyExistedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Add a new transaction to the database. This method should be invoked every time an Action related to creating a transaction is performed.
     * @param transaction The Transaction object to be added.
     * */
    public void addTransaction(Transaction transaction) {
        try {
            this.transactionData.addTransaction(transaction);
            System.out.println("Transaction added to database successfully.");
        } catch (TransactionAlreadyExistedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Revert an existing transaction.
     * used for test only, don't remove transactions, void them instead
     * @author Wang Mu Tian
     * @param transaction The Transaction object to be removed.
     * */
    public void revertTransaction(Transaction transaction){
        boolean status = this.transactionData.deleteTransaction(transaction);
        if (status) {
            System.out.println("Transaction reverted successfully.");
        } else {
            System.out.println("Transaction doesn't exists.");
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
     * @return An ArrayList of Transaction objects that occurred within the specified date range. Return null if none found.
     * */
    public ArrayList<Transaction> filterTransactionsByDateRange(String accountID, LocalDateTime start, LocalDateTime end) {
        ArrayList<Transaction> res = null;
        try {
            res = this.transactionData.filterTransactionsByDateRange(accountID, start, end);
            System.out.println("Filtered transactions for account ID " + accountID +
                    " from " + start.toString() + " to " + end.toString() + " retrieved successfully.");
        } catch (TransactionNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    /**
     * Filter transactions for a given account that occurred after the specified time.
     * @param accountID The ID of the account whose transactions are to be filtered.
     * @param time The LocalDateTime after which transactions should be included.
     * @return An ArrayList of Transaction objects that occurred after the specified time, return null if none found.
     * */
    public ArrayList<Transaction> filterTransactionsByTime(String accountID, LocalDateTime time) {
        ArrayList<Transaction> res = null;
        try {
            res = this.transactionData.filterTransactionsByTime(accountID, time);
            System.out.println("Filtered transactions for account ID " + accountID +
                    " after " + time.toString() + " retrieved successfully.");
        } catch (TransactionNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    /**
     * Update an existing account in the database. This method should be invoked every time an Action related to updating an account is performed.
     * @param account The Account object with updated information.
     * */
    public void updateAccount(Account account) {
        try {
            this.accountData.updateAccount(account);
            System.out.println("Account updated successfully.");
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateUserInfo(User user) {
        try {
            this.userData.updateUserInfo(user);
            System.out.println("User updated successfully.");
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateUserEmail(String oldEmail, String newEmail) {
        try {
            this.userData.updateUserEmail(oldEmail, newEmail);
            System.out.println("User email updated successfully.");
        } catch (UserNotFoundException | UserAlreadyExistedException e) {
            System.out.println(e.getMessage());
        }
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

    /**
     * Print all users in the database.
     * */
    public void printUsers() {
        this.userData.printUsers();
    }

}

/**
 * AccountData class implementing FileProcessor interface
 * */
class AccountData implements FileProcessor{
    private static AccountData ad;
    private static final String PATH = "src/database/bank_data_files/account.json";
    HashMap<String, Account> accounts;
    private static final Type a_type = new TypeToken<HashMap<String, Account>>() {}.getType();

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
            this.accounts = getGson().fromJson(fr, a_type);
            fr.close();
        }
        catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        // If accounts is null (e.g., file was empty), initialize it as an empty HashMap
        if (this.accounts == null || this.accounts.isEmpty()) {
            this.accounts = new HashMap<>();
        }

    }

    @Override
    public void save() {
        // Save accounts HashMap data into JSON file
        FileWriter fw;
        try {
            fw = new FileWriter(PATH);
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

    public Account getAccountById(String accountID) throws InvalidAccountIDException {
        Account res = this.accounts.get(accountID);
        if (res != null) return res;
        else throw new InvalidAccountIDException("Account ID " + accountID + " not found.");
    }

    public ArrayList<Account> getAccountsByUserName(String username) throws InvalidUsernameException {
        ArrayList<Account> res = new ArrayList<>();
        for (Account account : this.accounts.values()) {
            String first = account.getCustomer().getFirstName();
            String last = account.getCustomer().getLastName();
            String fullName = first + " " + last;
            if (fullName.equals(username)) {
                res.add(account);
            }
        }
        if (!res.isEmpty()) return res;
        else throw new InvalidUsernameException("No account found with username: " + username);
    }

    public ArrayList<Account> getAccountsByEmail(String email) throws InvalidEmailException {
        ArrayList<Account> res = new ArrayList<>();
        for (Account account : this.accounts.values()) {
            String mail = account.getCustomer().getEmail();
            if (mail.equals(email)) {
                res.add(account);
            }
        }
        if (!res.isEmpty()) return res;
        else throw new InvalidEmailException("No account found with email: " + email);
    }

    public Account getAccountByEmail(String email){
         for (Account account : this.accounts.values()) {
            if (account.getCustomer().getEmail().equals(email)) {
                return account;
            }
        }
         return null;
    }

    public void addAccount(Account account) throws AccountAlreadyExistedException {
        if (this.accounts.containsKey(account.getAccountID())) {
            throw new AccountAlreadyExistedException("Account with ID " + account.getAccountID() + " already exists."
            + " Please use a different ID, or update the existing account.");
        }
        this.accounts.put(account.getAccountID(), account);
        this.save(); // Save changes to file
    }

    public void updateAccount(Account account) throws AccountNotFoundException {
        if (!this.accounts.containsKey(account.getAccountID())) {
            throw new AccountNotFoundException("Account with ID " + account.getAccountID() + " does not exist. " +
                    "If you want to add a new account, please use the corresponding method.");
        }
        this.accounts.put(account.getAccountID(), account);
        // We will have to update Transaction database as well since transactions are linked to accounts
        for (Transaction transaction : TransactionData.getTransactionData().transactions.values()) {
            if (transaction.getSender().getAccountID().equals(account.getAccountID())) {
                transaction.getSender().update(account);
            }
            if (transaction.getReceiver().getAccountID().equals(account.getAccountID())) {
                transaction.getReceiver().update(account);
            }
            TransactionData.getTransactionData().save();
        }
        this.save(); // Save changes to file
    }
}

/**
 * UserData class implementing FileProcessor interface.
 * This class loads and manages user data where users are Customer, Teller, or Admin.
 * */
class UserData implements FileProcessor{
    private static UserData rd;
    private static final String PATH = "src/database/bank_data_files/user.json";
    HashMap<String, User> users;
    private static final Type u_type = new TypeToken<HashMap<String, User>>() {}.getType();

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
        // Load user data from JSON file into users HashMap
        FileReader fr = null;
        try {
            fr = new FileReader(PATH);
            this.users = getGson().fromJson(fr, u_type);
            fr.close();
        }
        catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        // If accounts is null (e.g., file was empty), initialize it as an empty HashMap
        if (this.users == null || this.users.isEmpty()) {
            this.users = new HashMap<>();
        }
    }

    @Override
    public void save() {
        // Save users HashMap data into JSON file
        FileWriter fw;
        try {
            fw = new FileWriter(PATH);
            getGson().toJson(this.users, u_type, fw);
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
                .registerTypeAdapter(User.class, new UserAdapter())
                .create();
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        for (User user : this.users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        throw new UserNotFoundException("User with email " + email + " not found.");
    }

    public void addUser(User user) throws UserAlreadyExistedException {
        if (this.users.containsKey(user.getEmail())) {
            throw new UserAlreadyExistedException("User with email " + user.getEmail() + " already exists."
                    + " Please use a different email.");
        }
        this.users.put(user.getEmail(), user);
        this.save(); // Save changes to file
    }

    /**
     * Update the email of an existing user in the database. This method should be invoked every time an Action related to updating a user's email is performed.
     * IMPORTANT: This method updates the email of the user across all related accounts and transactions. Use updateUserInfo method to update other user information.
     * @param oldEmail The current email of the user.
     * @param newEmail The new email to be set for the user.
     * */
    public void updateUserEmail(String oldEmail, String newEmail) throws UserNotFoundException, UserAlreadyExistedException {
        if (!this.users.containsKey(oldEmail)) {
            throw new UserNotFoundException("User with email " + oldEmail + " does not exist. " +
                    "If you want to add a new user, please use the corresponding method.");
        }
        if (this.users.containsKey(newEmail)) {
            throw new UserAlreadyExistedException("User with email " + newEmail + " already exists."
                    + " Please use a different email.");
        }
        User user = this.users.remove(oldEmail);
        user.setEmail(newEmail);
        this.users.put(newEmail, user);

        // Update accounts
        for (Account account : AccountData.getAccountData().accounts.values()) {
            if (account.getCustomer().getEmail().equals(oldEmail)) {
                account.getCustomer().setEmail(newEmail);
            }
        }
        AccountData.getAccountData().save();

        // Update transactions
        for (Transaction transaction : TransactionData.getTransactionData().transactions.values()) {
            if (transaction.getSender().getCustomer().getEmail().equals(oldEmail)) {
                transaction.getSender().getCustomer().setEmail(newEmail);
            }
            if (transaction.getReceiver().getCustomer().getEmail().equals(oldEmail)) {
                transaction.getReceiver().getCustomer().setEmail(newEmail);
            }
        }
        TransactionData.getTransactionData().save();
        this.save(); // Save changes to file
    }

    /**
     * Update an existing user in the database. This method should be invoked every time an Action related to updating a user information is performed.
     * IMPORTANT: This method does NOT update the email of the user. To update email, please use updateUserEmail method.
     * @param user The User object with updated information.
     * */
    public void updateUserInfo(User user) throws UserNotFoundException {
        if (!this.users.containsKey(user.getEmail())) {
            throw new UserNotFoundException("User with email " + user.getEmail() + " does not exist. " +
                    "If you want to add a new user, please use the corresponding method.");
        }
        this.users.put(user.getEmail(), user);
        // Update accounts
        for (Account account : AccountData.getAccountData().accounts.values()) {
            if (account.getCustomer().getEmail().equals(user.getEmail())) {
                account.getCustomer().setEmail(user.getEmail());
            }
        }
        AccountData.getAccountData().save();

        // Update transactions
        for (Transaction transaction : TransactionData.getTransactionData().transactions.values()) {
            if (transaction.getSender().getCustomer().getEmail().equals(user.getEmail())) {
                transaction.getSender().getCustomer().setEmail(user.getEmail());
            }
            if (transaction.getReceiver().getCustomer().getEmail().equals(user.getEmail())) {
                transaction.getReceiver().getCustomer().setEmail(user.getEmail());
            }
        }
        TransactionData.getTransactionData().save();
        this.save(); // Save changes to file
    }

    public void printUsers() {
        for (String key : this.users.keySet()) {
            System.out.println(users.get(key));
            System.out.println();
        }
    }
}

/**
 * TransactionData class implementing FileProcessor interface
 * */
class TransactionData implements FileProcessor {
    private static TransactionData td;
    private static final String PATH = "src/database/bank_data_files/transaction.json";
    HashMap<String, Transaction> transactions;
    private static final Type t_type = new TypeToken<HashMap<String, Transaction>>() {
    }.getType();

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
        // Load transaction data from JSON file into transactions HashMap
        FileReader fr = null;
        try {
            fr = new FileReader(PATH);
            this.transactions = getGson().fromJson(fr, t_type);
            fr.close();
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        // If accounts is null (e.g., file was empty), initialize it as an empty HashMap
        if (this.transactions == null || this.transactions.isEmpty()) {
            this.transactions = new HashMap<>();
        }
    }

    @Override
    public void save() {
        // Save accounts HashMap data into JSON file
        FileWriter fw;
        try {
            fw = new FileWriter(PATH);
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

    /**
     * Print all transactions in the database.
     * */
    public void printTransactions() {
        for (String key : this.transactions.keySet()) {
            System.out.println(transactions.get(key));
        }
    }

    /**
     * Retrieve a transaction by its ID.
     * @param transactionID The ID of the transaction to be retrieved.
     * @return The Transaction object with the specified ID.
     * @throws TransactionNotFoundException if the transaction with the specified ID is not found.
     * */
    public Transaction getTransactionById(String transactionID) throws TransactionNotFoundException {
        Transaction res = this.transactions.get(transactionID);
        if (res != null) return res;
        else throw new TransactionNotFoundException("Transaction ID " + transactionID + " not found.");
    }

    /**
     * Add a new transaction to the database. This method should be invoked every time an Action related to creating a transaction is performed.
     * @param transaction The Transaction object to be added.
     * */
    public void addTransaction(Transaction transaction) throws TransactionAlreadyExistedException {
        if (this.transactions.containsKey(transaction.getId())) {
            throw new TransactionAlreadyExistedException("Transaction with ID " + transaction.getId() + " already exists."
                    + " Please use a different ID.");
        }
        this.transactions.put(transaction.getId(), transaction);
        this.save(); // Save changes to file
    }

    /**
     * Delete an existing transaction from the database. This method should be invoked every time an Action related to deleting a transaction is performed.
     * @param transaction The Transaction object to be deleted.
     * @return true if the transaction was successfully deleted, false if the transaction does not exist.
     * */
    public boolean deleteTransaction(Transaction transaction){
        if (!this.transactions.containsKey(transaction.getId())) {
            return false;
        }
        this.transactions.remove(transaction.getId());
        this.save();
        return true;
    }

    /**
     * Retrieve all transactions for a given account ID.
     * @param accountID The ID of the account whose transactions are to be retrieved.
     * @return An ArrayList of Transaction objects associated with the given account ID.
     * @throws TransactionNotFoundException if no transactions are found for the given account ID.
     * @throws InvalidAccountIDException if the provided account ID is invalid.
     * */
    public ArrayList<Transaction> getTransactionsByAccountId(String accountID) throws TransactionNotFoundException, InvalidAccountIDException {
        try {
            Account account = AccountData.getAccountData().getAccountById(accountID);
            ArrayList<Transaction> result = new ArrayList<>();
            for (Transaction transaction : this.transactions.values()) {
                if (transaction.getSender().getAccountID().equals(accountID) ||
                        transaction.getReceiver().getAccountID().equals(accountID)) {
                    result.add(transaction);
                }
            }
            if (result.isEmpty()) {
                throw new TransactionNotFoundException("Account ID " + accountID + " has no associated transactions or doesn't exist.");
            }
            return result;
        }
        catch (InvalidAccountIDException e) {
            throw new InvalidAccountIDException("Account ID " + accountID + " is invalid.");
        }
    }

    /**
     * Filter transactions for a given account that occurred within the specified date range.
     * @param accountID The ID of the account whose transactions are to be filtered.
     * @param start The start LocalDateTime of the date range.
     * @param end The end LocalDateTime of the date range.
     * @return An ArrayList of Transaction objects that occurred within the specified date range.
     * @throws TransactionNotFoundException if no transactions are found within the specified date range.
     * */
    public ArrayList<Transaction> filterTransactionsByDateRange(String accountID, LocalDateTime start, LocalDateTime end) throws TransactionNotFoundException{
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction transaction : this.transactions.values()) {
            if ((transaction.getSender().getAccountID().equals(accountID) ||
                 transaction.getReceiver().getAccountID().equals(accountID)) &&
                (transaction.getTimeOfTransaction().isAfter(start) || transaction.getTimeOfTransaction().isEqual(start)) &&
                (transaction.getTimeOfTransaction().isBefore(end) || transaction.getTimeOfTransaction().isEqual(end))) {
                result.add(transaction);
            }
        }
        if (result.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found for Account ID " + accountID +
                    " within the specified date range.");
        }
        return result;
    }

    /**
     * Filter transactions for a given account that occurred after the specified time.
     * @param accountID The ID of the account whose transactions are to be filtered.
     * @param time The LocalDateTime after which transactions should be included.
     * @return An ArrayList of Transaction objects that occurred after the specified time.
     * @throws TransactionNotFoundException if no transactions are found after the specified time.
     * */
    public ArrayList<Transaction> filterTransactionsByTime(String accountID, LocalDateTime time) throws TransactionNotFoundException{
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction transaction : this.transactions.values()) {
            if ((transaction.getSender().getAccountID().equals(accountID) ||
                    transaction.getReceiver().getAccountID().equals(accountID)) &&
                    transaction.getTimeOfTransaction().isAfter(time)) {
                result.add(transaction);
            }
        }
        if (result.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found for Account ID " + accountID +
                    " after the specified time.");
        }
        return result;
    }

    /**
     * Update an existing transaction in the database. This method should be invoked every time an Action related to updating a transaction is performed.
     * @param transaction The Transaction object with updated information.
     * @param status The new status to be set for the transaction.
     * */
    public void updateTransactionStatus(Transaction transaction, Transaction.TransactionStatus status) throws TransactionNotFoundException {
        if (!this.transactions.containsKey(transaction.getId())) {
            throw new TransactionNotFoundException("Transaction with ID " + transaction.getId() + " does not exist. " +
                    "If you want to add a new transaction, please use the corresponding method.");
        }
        transaction.setStatus(status);
        this.transactions.put(transaction.getId(), transaction);
        this.save(); // Save changes to file
    }
}

/**
 * BranchData class implementing FileProcessor interface
 * */
class BranchData implements FileProcessor{
    private static BranchData bd;
    private static final String PATH = "src/database/bank_data_files/branch.json";
    private HashMap<String, Branch> branches;
    private static final Type b_type = new TypeToken<HashMap<String, Branch>>() {
    }.getType();

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
        FileReader fr = null;
        try {
            fr = new FileReader(PATH);
            this.branches = getGson().fromJson(fr, b_type);
            fr.close();
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        // If accounts is null (e.g., file was empty), initialize it as an empty HashMap
        if (this.branches == null || this.branches.isEmpty()) {
            this.branches = new HashMap<>();
        }
    }

    @Override
    public void save() {
        // Save accounts HashMap data into JSON file
        FileWriter fw;
        try {
            fw = new FileWriter(PATH);
            getGson().toJson(this.branches, b_type, fw);
            fw.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public Gson getGson() {
        return null;
    }
}
