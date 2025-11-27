package UI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import auth.core.Customer;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.*;
import com.google.gson.Gson;

import core.ExecuteTransactionAction;
import core.LoginAction;
import core.ProfileAction;
import core.ViewTransactionAction;
import core.exceptions.InvalidAccountException;
import core.exceptions.InvalidInputException;
import database.DatabaseSingleton;

// ---------- Models / Store (users + roles from JSON) ----------
//class User {
//    String user_id;
//    String password;
//    String role;
//}
//
//class UsersFile {
//    List<User> users = new ArrayList<>();
//}
//
//class UserJsonStore {
//    private final Map<String, User> byId = new HashMap<>();
//
//    UserJsonStore(String path) {
//        try {
//            String json = Files.readString(Paths.get(path));
//            UsersFile uf = new Gson().fromJson(json, UsersFile.class);
//            if (uf != null && uf.users != null)
//                for (User u : uf.users) byId.put(u.user_id.toLowerCase(Locale.ROOT), u);
//        } catch (Exception e) {
//            throw new RuntimeException("Load users.json failed", e);
//        }
//    }
//
//    User find(String id) {
//        return id == null ? null : byId.get(id.toLowerCase(Locale.ROOT));
//    }
//}

// ---------- App ----------
public class BankUIDemo {
    private final JFrame frame = new JFrame("Bank");
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private String currentRole;
    protected User currentUser;
    private LoginPage loginPage;
    private ProfileAction profile;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankUIDemo().start());
    }

    private void start() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // pages
        // Storing LoginPage instance for loginPage.reset() calls
        loginPage = new LoginPage();
        root.add(loginPage, "login");

        // customer
        root.add(new CustomerDashboard(), "customer");

        // customer account info
        root.add(new CustomerAccountInfo(), "cust_account");
        root.add(new TransactionHistory(), "cust_account_transactions");
        root.add(new CustomerAccountSummary(), "cust_account_summary");
        //customer personal info
        root.add(new CustomerProfile(), "cust_profile");


        // teller
        root.add(new TellerDashboard(), "teller");
        root.add(new TellerManageCustomers(), "teller_manage");

        // admin
        root.add(new AdminDashboard(), "admin");
        root.add(new AdminUserMgmt(), "admin_user_mgmt");

        // Fund transfer & Withdraw/Deposit
        root.add(new FundTransferUI(), "fund_transfer");
        root.add(new WithdrawDepositUI(), "withdraw_deposit");

        cards.show(root, "login");
        frame.setContentPane(root);
        frame.setPreferredSize(new Dimension(390, 250)); // bigger window

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private void go(String page) {
        cards.show(root, page);
    }

    // ---------- Login ----------
    class LoginPage extends JPanel {
        JTextField tfUser = new JTextField(22);
        JPasswordField pfPass = new JPasswordField(22);
        LoginAction loginAction;

        LoginPage() {
            super(new GridBagLayout());
            setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            GridBagConstraints c = gbc();

            JLabel title = title("Login");
            c.gridy = 0;
            c.gridwidth = 2;
            add(title, c);

            row(this, c, 1, "User ID", tfUser);
            row(this, c, 2, "Password", pfPass);

            JButton signIn = new JButton(new AbstractAction("Sign In") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    doLogin();
                    switch (currentUser.getClass().getSimpleName().toLowerCase()) {
                        case "customer" -> go("customer");
                        case "teller" -> go("teller");
                        case "admin" -> go("admin");
                        default -> toast("Unknown role: " + currentRole);
                    }
                }
            });
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            actions.add(signIn);
            c.gridy = 3;
            c.gridx = 0;
            c.gridwidth = 2;
            add(actions, c);
        }

        void doLogin() {
            String email = tfUser.getText().trim();
            String password = new String(pfPass.getPassword());

            loginAction = new LoginAction();
            loginAction.setEmail(email);
            loginAction.setPassword(password);
            try {
                loginAction.execute();
            } catch (InvalidAuthenticationException e) {
                toast(e.getMessage());
                return;
            }
            currentUser = loginAction.getAuthenticatedUser();

            //Loads the data to the profile action
        }

        void reset() {
            tfUser.setText("");
            pfPass.setText("");
        }
    }

    // ---------- Customer ----------
    class CustomerDashboard extends JPanel {
        CustomerDashboard() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("Customer Dashboard"), g(c, 0, 0, 2));

            JButton account = btn("Account Information", () -> go("cust_account"));
            JButton profile = btn("Personal Profile", () -> go("cust_profile"));
            JButton logout = btn("Logout", () -> {
                currentUser = null;
                loginPage.reset();
                go("login");

            });

            add(account, g(c, 0, 1, 2));
            add(profile, g(c, 0, 2, 2));
            add(logout, g(c, 0, 3, 2));
        }
    }


    class CustomerAccountInfo extends JPanel {
        CustomerAccountInfo() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("Account Information"), g(c, 0, 0, 2));

            add(btn("Transaction History", () -> go("cust_account_transactions")));
            add(btn("Account Summary", () -> go("cust_account_summary")), g(c, 0, 2, 2));
            add(btn("Fund Transfer", () -> go("fund_transfer")), g(c, 0, 3, 2));
            add(btn("Deposit / Withdraw", () -> go("withdraw_deposit")), g(c, 0, 4, 2));
            add(btn("Back", () -> go("customer")), g(c, 0, 5, 2));

        }
    }

    /***
     * Account Summary
     *
     */
    class CustomerAccountSummary extends JPanel {
        //private ProfileAction profile;

        CustomerAccountSummary() {
            super(new GridBagLayout());
            setBorder(pad());
            addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    displaySummary();   // ← RUN UPDATE HERE
                    revalidate();
                    repaint();
                }
            });
        }

        private void displaySummary() {
            removeAll();
            profile = new ProfileAction();
            profile.setCurrentUser(currentUser);

            try {
                profile.execute();
            } catch (InvalidAuthenticationException | InvalidAccountException e) {
                throw new RuntimeException(e);
            }

            GridBagConstraints c = gbc();
            add(title("Account Summary"), g(c, 0, 0, 2));
            add(btn("Back", () -> go("cust_account")), g(c, 0, 5, 2));
            if (profile.getUserAccount() != null) {
                add(new JLabel("Account Number: " + profile.getUserAccount().getAccountID()), g(c, 0, 1, 2));
                if (profile.getUserAccount() instanceof Card) { //Credit Card
                    Card cc = (Card) profile.getUserAccount();
                    add(new JLabel("Account Type: Credit Card"), g(c, 0, 2, 2));
                    add(new JLabel("Credit Limit: $" + cc.getCreditLimit()), g(c, 0, 3, 2));
                    add(new JLabel("Credit Usage: $" + cc.getCreditUsage()), g(c, 0, 4, 2));
                } else if (profile.getUserAccount() instanceof Saving) { //Saving
                    add(new JLabel("Account Type: Saving"), g(c, 0, 2, 2));
                    add(new JLabel("Balance: $" + profile.getUserAccount().getBalance()), g(c, 0, 3, 2));
                } else {//Chequing
                    add(new JLabel("Account Type: Chequing"), g(c, 0, 2, 2));
                    add(new JLabel("Balance: $" + profile.getUserAccount().getBalance()), g(c, 0, 3, 2));
                }
            } else {
                add(new JLabel("No account found"), g(c, 0, 1, 2));
            }
        }

    }

    class FundTransferUI extends JPanel {
        private Account currentAccount;
        JComboBox<String> senderAccountSelector = new JComboBox<>();

        JTextField recipientEmail = new JTextField(22);
        JComboBox<String> recipientAccountSelector = new JComboBox<>();

        JTextField amountInput = new JTextField(22);

        FundTransferUI() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();

            addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    TransferUI();
                    revalidate();
                    repaint();
                }
            });
        }

        private void TransferUI(){
            GridBagConstraints c = gbc();
            resetForm();

            add(title("Fund Transfer"), g(c, 0, 0, 2));
            senderAccountSelector.setEnabled(true);
            row(this, c, 1, "Sender Account:", senderAccountSelector);


            row(this, c, 3, "Recipient: ", recipientEmail);
            JButton searchBtn = btn("Search", () -> {
                fetchSenderAccount();
                findRecipient();
            });

            add(searchBtn, g(c, 0, 4, 2) );

            // Account chooser (initially disabled)
            recipientAccountSelector.setEnabled(false);
            row(this, c, 5, "Recipient Account:", recipientAccountSelector);
            row(this, c, 6, "Amount: ", amountInput);


            JButton confirmBtn = btn("Confirm", this::conductTransfer);
            add(confirmBtn, g(c, 0, 7, 2));
            add(btn("Back", () -> go("cust_account")), g(c, 0, 8, 2));
        }

        private void resetForm(){
            senderAccountSelector.removeAllItems();
            recipientEmail.setText("");
            recipientAccountSelector.removeAllItems();
            recipientAccountSelector.setEnabled(false);
            amountInput.setText("");

            ArrayList<Account> myAccounts = DatabaseSingleton.getDatabase().getAccountsByEmail(currentUser.getEmail());
            for (Account acc : myAccounts){
                senderAccountSelector.addItem(String.valueOf(acc.getAccountID()));
            }
        }

        //This part currently is redundant
        //Needs the proper fixes before it can work for many accounts of the same user
        private void fetchSenderAccount(){
            profile = new ProfileAction();
            profile.setCurrentUser(currentUser);

            try {
                profile.execute();
                currentAccount = profile.getUserAccount();
            } catch (InvalidAuthenticationException | InvalidAccountException e) {
                throw new RuntimeException(e);
            }
        }

        private void findRecipient(){
            try {
                String email = recipientEmail.getText().trim();
                User recipient = DatabaseSingleton.getDatabase().getUserByEmail(email);

                if (recipient == null){
                    toast("Email not found");
                    recipientAccountSelector.setEnabled(false);
                    recipientAccountSelector.removeAllItems();
                    return;
                }

                ArrayList<Account> accounts = DatabaseSingleton.getDatabase().getAccountsByEmail(email);

                recipientAccountSelector.removeAllItems();
                for(Account acc: accounts){
                    if (acc instanceof Chequing){
                        recipientAccountSelector.addItem("Chequing: " + acc.getAccountID());
                    }
                    if (acc instanceof Saving){
                        recipientAccountSelector.addItem("Saving: " + acc.getAccountID());
                    }
                    else{
                        recipientAccountSelector.addItem("Credit Card: " + acc.getAccountID());
                    }

                }
                recipientAccountSelector.setEnabled(true);

            }catch (Exception e){
                toast("Invalid destination account");
            }
        }

        private boolean conductTransfer(){
            try{

                double amount = Double.parseDouble(amountInput.getText());

                String selected = (String) recipientAccountSelector.getSelectedItem();
                String parts[] = selected.split(":");

                Account destinationAcc = DatabaseSingleton.getDatabase().getAccountByID(parts[1].trim());

                Transaction transaction = new Transaction(profile.getUserAccount(), destinationAcc, LocalDateTime.now(), amount);

                ExecuteTransactionAction sendAction = new ExecuteTransactionAction();
                sendAction.setUser(currentUser);
                sendAction.setTransactionDetails(transaction);
                sendAction.prepare();
                sendAction.execute();

                toast("Transaction Sent");
                return true; //signals the button

            }catch (InvalidInputException ie){
                toast("Invalid recipient");
                return false;
            }
            catch (Exception e) {
                toast("Invalid amount");
                return false; //signals the button
            }
        }
    }

    class WithdrawDepositUI extends JPanel {
        String[] choices = {"Withdraw", "Deposit"};
        JComboBox<String> box = new JComboBox<>(choices);
        JTextField amountInput = new JTextField(22);


        WithdrawDepositUI() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("Withdraw/Deposit"), g(c, 0, 0, 2));
            add(box, g(c, 0, 1, 2));
            row(this, c, 2, "Amount: ", amountInput);

            //Just need to adapt this part using the fundTransfer

            add(btn("Confirm", () ->info("Confirm Withdraw/Deposit")), g(c, 0, 4, 2));
            add(btn("Back", () -> go("cust_account")), g(c, 0, 5, 2));
        }
    }

    /***
     * TransactionHistory
     * UI Panel + fetching transactions + data refresh trigger
     * updateData() - fetch the data from database through {@link ViewTransactionAction}
     * addComponentListener() + componentShown() - refresh data when this panel is displayed (through go())
     * @see ViewTransactionAction
     * @author Wang Mu Tian
     */
    class TransactionHistory extends JPanel {
        final String titleText = "Transaction History";
        final TransactionTable tableModel = new TransactionTable();
        final JTable viewTable = new JTable(tableModel);
        protected ViewTransactionAction viewTransactionAction;

        // Initialize the panel to see the transaction history of an account
        TransactionHistory() {
            super(new GridBagLayout());
            setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            GridBagConstraints c = gbc();

            // ----- Title -----
            JLabel title = new JLabel(this.titleText);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
            add(title, g(c, 0, 0, 2));

            // ----- Table -----
            viewTable.setFillsViewportHeight(true);
            viewTable.setAutoCreateRowSorter(true);

            JScrollPane scroll = new JScrollPane(viewTable);
            GridBagConstraints tableC = g(c, 0, 1, 2);
            tableC.weighty = 1;
            tableC.fill = GridBagConstraints.BOTH;
            add(scroll, tableC);

            // ----- Footer Buttons -----
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton backBtn = new JButton("Back");

            backBtn.addActionListener(e -> go("cust_account"));
            actions.add(backBtn);
            add(actions, g(c, 0, 2, 2));

            // Fetch trigger
            addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    updateData();   // ← RUN UPDATE HERE
                }
            });
        }

        public void updateData() {
            // Setup data
            viewTransactionAction = new ViewTransactionAction();
            viewTransactionAction.setUser(currentUser);
            viewTransactionAction.setAccountViewed(DatabaseSingleton.getDatabase().getAccountByUser(currentUser));
            try {
                viewTransactionAction.execute();
            } catch (InvalidAuthenticationException | InvalidAccountException e) {
                // TODO: Manage edge cases and error cases
                throw new RuntimeException(e);
            }
            tableModel.setTransactions(viewTransactionAction.getListOfTransactions());
        }

        class TransactionTable extends AbstractTableModel {
            private final String[] columns = {"Transaction ID", "Amount", "Time"};
            private List<Transaction> data = new ArrayList<>();

            public void setTransactions(List<Transaction> data) {
                this.data = data;
                fireTableDataChanged(); // tells JTable to repaint
            }

            @Override
            public int getRowCount() {
                return data.size();
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public String getColumnName(int column) {
                return columns[column];
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Transaction t = data.get(rowIndex);
                return switch (columnIndex) {
                    case 0 -> t.getId();
                    case 1 -> t.getAmountForAccount(viewTransactionAction.getAccountViewed());
                    case 2 -> t.getTimeOfTransaction();
                    default -> "";
                };
            }
        }
    }

    class CustomerProfile extends JPanel {
        CustomerProfile() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("Personal Profile"), g(c, 0, 0, 2));

            add(btn("Update Personal Information", () -> info("Open Update Info")), g(c, 0, 1, 2));
            add(btn("Change Password", () -> info("Open Change Password")), g(c, 0, 2, 2));
            add(btn("Back", () -> go("customer")), g(c, 0, 3, 2));
        }
    }

    // ---------- Teller ----------
    class TellerDashboard extends JPanel {
        TellerDashboard() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("Teller Dashboard"), g(c, 0, 0, 2));

            add(btn("Manage Customers", () -> go("teller_manage")), g(c, 0, 1, 2));
            add(btn("Logout", () -> {
                currentUser = null;
                loginPage.reset();
                go("login");
            }), g(c, 0, 2, 2));
        }
    }

    class TellerManageCustomers extends JPanel {
        TellerManageCustomers() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("Manage Customers"), g(c, 0, 0, 2));

            add(btn("View Customer Information", () -> info("Open View Customer Information")), g(c, 0, 1, 2));
            add(btn("View All Transactions", () -> info("Open All Transactions")), g(c, 0, 2, 2));
            add(btn("Back", () -> go("teller")), g(c, 0, 3, 2));
        }
    }

    // ---------- Admin ----------
    class AdminDashboard extends JPanel {
        AdminDashboard() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("Admin Dashboard"), g(c, 0, 0, 2));

            add(btn("User Management", () -> go("admin_user_mgmt")), g(c, 0, 1, 2));
            add(btn("Logout", () -> {
                currentUser = null;
                loginPage.reset();
                go("login");
            }), g(c, 0, 2, 2));
        }
    }

    class AdminUserMgmt extends JPanel {
        AdminUserMgmt() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("User Management"), g(c, 0, 0, 2));

            add(btn("Create Accounts", () -> info("Open Create Accounts")), g(c, 0, 1, 2));
            add(btn("Reset Account Passwords", () -> info("Open Reset Passwords")), g(c, 0, 2, 2));
            add(btn("Deactivate Accounts", () -> info("Open Deactivate Accounts")), g(c, 0, 3, 2));
            add(btn("View All Transactions", () -> info("Open All Transactions")), g(c, 0, 4, 2));
            add(btn("Back", () -> go("admin")), g(c, 0, 5, 2));
        }
    }

    // ---------- UI helpers ----------
    private static GridBagConstraints gbc() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        return c;
    }

    private static GridBagConstraints g(GridBagConstraints c, int x, int y, int w) {
        GridBagConstraints n = (GridBagConstraints) c.clone();
        n.gridx = x;
        n.gridy = y;
        n.gridwidth = w;
        return n;
    }

    private static JButton btn(String t, Runnable r) {
        return new JButton(new AbstractAction(t) {
            @Override
            public void actionPerformed(ActionEvent e) {
                r.run();
            }
        });
    }

    private static JLabel title(String t) {
        JLabel l = new JLabel(t);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 20f));
        return l;
    }

    private static Border pad() {
        return BorderFactory.createEmptyBorder(16, 16, 16, 16);
    }

    private static void row(JPanel p, GridBagConstraints base, int r, String lab, JComponent field) {
        // label
        GridBagConstraints c = (GridBagConstraints) base.clone();
        c.gridy = r;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(12, 12, 12, 24);   // ↑ top,left,bottom,RIGHT GAP (24)
        p.add(new JLabel(lab), c);

        // field
        c = (GridBagConstraints) base.clone();
        c.gridy = r;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(12, 96, 12, 12);
        p.add(field, c);
    }

    private static void toast(String m) {
        JOptionPane.showMessageDialog(null, m, "Notice", JOptionPane.WARNING_MESSAGE);
    }

    private static void info(String m) {
        JOptionPane.showMessageDialog(null, m, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}

