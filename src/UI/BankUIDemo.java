package UI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import auth.core.Admin;
import auth.core.Customer;
import auth.core.Teller;
import auth.core.User;
import auth.exceptions.InvalidAuthenticationException;
import bank.*;

import core.*;
import core.ExecuteTransactionAction;
import core.LoginAction;
import core.ProfileAction;
import core.ViewTransactionAction;
import core.exceptions.InsufficientFundsException;
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

    //For the ATM
    private static Account ATM;

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
        root.add(new CustomerProfileUpdate(), "cust_profile_update");
        root.add(new CustomerPasswordUpdate(), "cust_password_update");


        // teller
        root.add(new TellerDashboard(), "teller");
        root.add(new TellerManageCustomers(), "teller_manage");
        root.add(new TellerViewAccounts(), "teller_view_accounts");
        root.add(new TellerViewTransactions(), "teller_view_transactions");

        // admin
        root.add(new AdminDashboard(), "admin");
        root.add(new AdminUserMgmt(), "admin_user_mgmt");
        root.add(new AdminCreateAccount(), "admin_user_mgmt_create_account");
        root.add(new AdminViewTransactions(), "admin_user_mgmt_view_transactions");
        root.add(new AdminUpdatePassword(), "admin_user_mgmt_update_password");
        root.add(new AdminDeactivateAccount(), "admin_user_mgmt_deactivate_account");

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
                    if (currentUser == null) {
                        return; // Block dashboard access before login successfully.
                    }
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
            add(btn("Transaction History", () -> go("cust_account_transactions")), g(c, 0, 1, 2));
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
        private final AccountComboBox accountDropDown;
        private ArrayList<Account> accounts;
        private Account selectedAccount;


        CustomerAccountSummary() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();


            add(title("Account Summary"), g(c, 0, 0, 2));

            add(new JLabel("Select Account:"), g(c, 0, 1, 1));
            accountDropDown = new AccountComboBox();
            add(accountDropDown, g(c, 0, 2, 1));


            if (selectedAccount != null) {
                add(new JLabel("Account Number: " + selectedAccount.getAccountID()), g(c, 0, 4, 2));

                if (selectedAccount instanceof Card) { //Credit Card
                    add(new JLabel("Account Type: Credit Card"), g(c, 0, 5, 2));
                    add(new JLabel("Credit Limit: $" + ((Card) selectedAccount).getCreditLimit()), g(c, 0, 6, 2));
                    add(new JLabel("Credit Usage: $" + ((Card) selectedAccount).getCreditUsage()), g(c, 0, 7, 2));
                } else if (selectedAccount instanceof Saving) { //Saving
                    add(new JLabel("Account Type: Saving"), g(c, 0, 5, 2));
                    add(new JLabel("Balance: $" + selectedAccount.getBalance()), g(c, 0, 6, 2));
                } else {//Chequing
                    add(new JLabel("Account Type: Chequing"), g(c, 0, 5, 2));
                    add(new JLabel("Balance: $" + selectedAccount.getBalance()), g(c, 0, 6, 2));
                }
            }

            add(btn("Back", () -> go("cust_account")), g(c, 0, 7, 2));

            addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    loadAccounts();
                    revalidate();
                    repaint();
                }
            });

            // This is when an new account is selected to view summary
            accountDropDown.addActionListener(e -> {
                selectedAccount = accountDropDown.getSelectAccount();
                if (selectedAccount != null) {
                    add(new JLabel("Account Number: " + selectedAccount.getAccountID()), g(c, 0, 4, 2));

                    if (selectedAccount instanceof Card) { //Credit Card
                        add(new JLabel("Account Type: Credit Card"), g(c, 0, 5, 2));
                        add(new JLabel("Credit Limit: $" + ((Card) selectedAccount).getCreditLimit()), g(c, 0, 6, 2));
                        add(new JLabel("Credit Usage: $" + ((Card) selectedAccount).getCreditUsage()), g(c, 0, 7, 2));
                    } else if (selectedAccount instanceof Saving) { //Saving
                        add(new JLabel("Account Type: Saving"), g(c, 0, 5, 2));
                        add(new JLabel("Balance: $" + selectedAccount.getBalance()), g(c, 0, 6, 2));
                    } else {//Chequing
                        add(new JLabel("Account Type: Chequing"), g(c, 0, 5, 2));
                        add(new JLabel("Balance: $" + selectedAccount.getBalance()), g(c, 0, 6, 2));
                    }
                }


                revalidate();
                repaint();
            });
        }

        private void loadAccounts() {
            ProfileAction profile = new ProfileAction();
            profile.setCurrentUser(currentUser);

            try {
                profile.execute();
            } catch (InvalidAuthenticationException | InvalidAccountException e) {
                throw new RuntimeException(e);
            }
            accounts = profile.getUserAccount();
            accountDropDown.setAccounts(accounts);
        }

        static class AccountComboBox extends JComboBox<Account> {
            private final DefaultComboBoxModel<Account> model;

            public AccountComboBox() {
                super();
                model = new DefaultComboBoxModel<>();
                setModel(model);

                setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                        if (value instanceof Account account) {
                            setText(getDisplayText(account));
                        } else {
                            setText("");
                        }

                        return this;
                    }
                });
            }

            /**
             * Replace all users in the combo box.
             */
            public void setAccounts(List<Account> accounts) {
                model.removeAllElements();
                if (accounts == null) return;
                for (Account a : accounts) {
                    model.addElement(a);
                }
                if (model.getSize() > 0) {
                    setSelectedIndex(0);
                }
            }

            /**
             * Returns the User currently selected, or null.
             */
            public Account getSelectAccount() {
                Object sel = getSelectedItem();
                return (sel instanceof Account) ? (Account) sel : null;
            }

            private String getDisplayText(Account account) {
                return account.getClass().getSimpleName().toUpperCase() + ": $" + account.getBalance();
            }

        }

    }

    class FundTransferUI extends JPanel {
        // ComboBox for selection of sender and receiver for the transaction
        AccountComboBox senderAccountSelector = new AccountComboBox();
        AccountComboBox recipientAccountSelector = new AccountComboBox();

        // Search box for searching the account
        JTextField recipientEmail = new JTextField(22);

        JTextField amountInput = new JTextField(22);

        FundTransferUI() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();

            addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    fetchSenderAccount();
                    revalidate();
                    repaint();
                }
            });

            add(title("Fund Transfer"), g(c, 0, 0, 2));
            senderAccountSelector.setEnabled(true);
            row(this, c, 1, "Sender Account:", senderAccountSelector);

            row(this, c, 3, "Recipient: ", recipientEmail);
            JButton searchBtn = btn("Search", () -> {
                if (findRecipient()) {
                    revalidate();
                    repaint();
                }
            });

            add(searchBtn, g(c, 0, 4, 2));

            // Account chooser (initially disabled)
            recipientAccountSelector.setEnabled(false);
            row(this, c, 5, "Recipient Account:", recipientAccountSelector);
            row(this, c, 6, "Amount: ", amountInput);


            JButton confirmBtn = btn("Confirm", this::conductTransfer);
            add(confirmBtn, g(c, 0, 7, 2));
            add(btn("Back", () -> go("cust_account")), g(c, 0, 8, 2));


        }

        static class AccountComboBox extends JComboBox<Account> {
            private final DefaultComboBoxModel<Account> model;

            public AccountComboBox() {
                super();
                model = new DefaultComboBoxModel<>();
                setModel(model);

                setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                        if (value instanceof Account account) {
                            setText(getDisplayText(account));
                        } else {
                            setText("");
                        }

                        return this;
                    }
                });
            }

            /**
             * Replace all users in the combo box.
             */
            public void setAccounts(List<Account> accounts) {
                model.removeAllElements();
                if (accounts == null) return;
                for (Account a : accounts) {
                    model.addElement(a);
                }
                if (model.getSize() > 0) {
                    setSelectedIndex(0);
                }
            }

            /**
             * Returns the User currently selected, or null.
             */
            public Account getSelectAccount() {
                Object sel = getSelectedItem();
                return (sel instanceof Account) ? (Account) sel : null;
            }

            private String getDisplayText(Account account) {
                return account.getClass().getSimpleName().toUpperCase() + ": $" + account.getBalance();
            }
        }

        /**
         *
         */
        private void resetForm() {
            senderAccountSelector.removeAllItems();
            recipientEmail.setText("");
            recipientAccountSelector.removeAllItems();
            recipientAccountSelector.setEnabled(false);
            amountInput.setText("");
        }


        //Needs the proper fixes before it can work for many accounts of the same user (currently 1-1 relation)
        private void fetchSenderAccount() {
            ProfileAction profile = new ProfileAction();
            profile.setCurrentUser(currentUser);
            try {
                profile.prepare();
                profile.execute();
                senderAccountSelector.setAccounts(profile.getUserAccount());
            } catch (InvalidAuthenticationException | InvalidAccountException e) {
                toast("System Timeout");
            } catch (InvalidInputException e) {
                toast("Invalid Input");
            } catch (Exception e) {
                toast("Other exceptions");
            }
        }

        private boolean findRecipient() {
            ProfileAction action = new ProfileAction();
            try {
                String email = recipientEmail.getText().trim();
                User recipient = DatabaseSingleton.getDatabase().getUserByEmail(email);

                action.setCurrentUser(recipient);
                action.execute();
                if (recipient == null) {
                    toast("Email not found");
                    recipientAccountSelector.setEnabled(false);
                    recipientAccountSelector.removeAllItems();
                    return false;
                }
            } catch (InvalidAuthenticationException e) {
                throw new RuntimeException(e);
            } catch (InvalidAccountException e) {
                throw new RuntimeException(e);
            }
            recipientAccountSelector.setAccounts(action.getUserAccount());

//                ArrayList<Account> accounts = DatabaseSingleton.getDatabase().getAccountsByEmail(email);
//                recipientAccountSelector.removeAllItems();
//                for (Account acc : accounts) {
//                    if (acc instanceof Chequing) {
//                        recipientAccountSelector.addItem("Chequing: " + acc.getAccountID());
//                    }
//                    if (acc instanceof Saving) {
//                        recipientAccountSelector.addItem("Saving: " + acc.getAccountID());
//                    } else {
//                        recipientAccountSelector.addItem("Credit Card: " + acc.getAccountID());
//                    }
//                }
            recipientAccountSelector.setEnabled(true);

            return true;
        }

        private boolean conductTransfer() {
            try {
                double amount = Double.parseDouble(amountInput.getText());
                Transaction transaction = new Transaction(senderAccountSelector.getSelectAccount(), recipientAccountSelector.getSelectAccount(), LocalDateTime.now(), amount);
                ExecuteTransactionAction sendAction = new ExecuteTransactionAction();
                sendAction.setUser(currentUser);
                sendAction.setTransactionDetails(transaction);
                sendAction.prepare();
                sendAction.execute();
            } catch (InvalidInputException ie) {
                toast("Invalid recipient");
                return false;
            } catch (Exception e) {
                toast("Invalid amount");
                return false; //signals the button
            }
            toast("Transaction Sent");
            resetForm(); // Reset the form after a transaction is successfully executed.
            return true; //signals the button
        }
    }

    class WithdrawDepositUI extends JPanel {
        String[] choices = {"Withdraw", "Deposit"};
        JComboBox<String> box = new JComboBox<>(choices);
        JTextField amountInput = new JTextField(22);

        AccountComboBox accountBox = new AccountComboBox();

        WithdrawDepositUI() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("Withdraw/Deposit"), g(c, 0, 0, 2));
            add(box, g(c, 0, 1, 2));

            add(new JLabel("Select Account:"), g(c, 0, 1, 1));

            add(accountBox, g(c, 0, 2, 1));
            row(this, c, 3, "Amount: ", amountInput);

            addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    ProfileAction profile = new ProfileAction();
                    profile.setCurrentUser(currentUser);
                    try {
                        profile.execute();
                    } catch (InvalidAuthenticationException | InvalidAccountException ie) {
                        throw new RuntimeException(ie);
                    }
                    accountBox.setAccounts(profile.getUserAccount());
                    revalidate();
                    repaint();
                }
            });
            ATM = DatabaseSingleton.getDatabase().getAccountByID("8df41236-c149-4421-83e8-07a4e4618498");
            add(btn("Confirm", () -> {
                this.WithdrawDeposit();
                revalidate();
                repaint();
            }), g(c, 0, 4, 2));
            add(btn("Back", () -> go("cust_account")), g(c, 0, 5, 2));
        }

        class AccountComboBox extends JComboBox<Account> {
            private final DefaultComboBoxModel<Account> model;

            public AccountComboBox() {
                super();
                model = new DefaultComboBoxModel<>();
                setModel(model);

                setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                        if (value instanceof Account account) {
                            setText(getDisplayText(account));
                        } else {
                            setText("");
                        }

                        return this;
                    }
                });
            }

            /**
             * Replace all users in the combo box.
             */
            public void setAccounts(List<Account> accounts) {
                model.removeAllElements();
                if (accounts == null) return;
                for (Account a : accounts) {
                    model.addElement(a);
                }
                if (model.getSize() > 0) {
                    setSelectedIndex(0);
                }
            }

            /**
             * Returns the User currently selected, or null.
             */
            public Account getSelectAccount() {
                Object sel = getSelectedItem();
                return (sel instanceof Account) ? (Account) sel : null;
            }

            private String getDisplayText(Account account) {
                return account.getClass().getSimpleName().toUpperCase() + ": $" + account.getBalance();
            }
        }


        private void WithdrawDeposit() {
            String selected = (String) box.getSelectedItem();
            double amount = 0;
            try {
                amount = Double.parseDouble(amountInput.getText());
            } catch (NumberFormatException e) {
                toast("Invalid amount!");
                return;
            }
            ExecuteTransactionAction withdrawDepositAction = new ExecuteTransactionAction();
            Transaction transaction;
            String message;

            if (Objects.equals(selected, "Deposit")) {
                transaction = new Transaction(ATM, accountBox.getSelectAccount(), LocalDateTime.now(), amount);
                withdrawDepositAction.setUser(currentUser);
                message = "Money Deposited";
            } else {
                transaction = new Transaction(accountBox.getSelectAccount(), ATM, LocalDateTime.now(), amount);
                withdrawDepositAction.setUser(currentUser);
                message = "Money Withdrew";
            }
            withdrawDepositAction.setTransactionDetails(transaction);

            try {
                withdrawDepositAction.prepare();
            } catch (InvalidAuthenticationException e) {
                toast(e.getMessage());
                return;
            } catch (InvalidInputException e) {
                toast(e.getMessage());
                return;
            }

            try {
                withdrawDepositAction.execute();
            } catch (InvalidAuthenticationException e) {
                toast(e.getMessage());
            } catch (InvalidAccountException e) {
                toast(e.getMessage());
            } catch (InsufficientFundsException e) {
                toast(e.getMessage());
            }

            /*
                     toast(message);
            } catch (InsufficientFundsException e) {
                toast("Insufficient Balance");
            } catch (InvalidAuthenticationException iae) {
                toast("Invalid ID");
            } catch (InvalidAccountException e) {
                toast("Invalid account");
            }



             */

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

            add(btn("Update Personal Information", () -> go("cust_profile_update")), g(c, 0, 1, 2));
            add(btn("Change Password", () -> go("cust_password_update")), g(c, 0, 2, 2));
            add(btn("Back", () -> go("customer")), g(c, 0, 3, 2));
        }
    }

    class CustomerProfileUpdate extends JPanel {
        CustomerProfileUpdate() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("Personal Profile Update"), g(c, 0, 0, 2));


            add(btn("Back", () -> go("cust_profile")), g(c, 0, 3, 2));
        }
    }


    class CustomerPasswordUpdate extends JPanel {
        private final JPasswordField currentPasswordField;
        private final JPasswordField newPasswordField;
        private final JPasswordField confirmPasswordField;
        private ArrayList<User> users = null;

        CustomerPasswordUpdate() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("Change Password"), g(c, 0, 0, 2));

            add(new JLabel("Current Password:"), g(c, 0, 2, 1));
            currentPasswordField = new JPasswordField(15);
            add(currentPasswordField, g(c, 1, 2, 1));

            // New Password
            add(new JLabel("New Password:"), g(c, 0, 3, 1));
            newPasswordField = new JPasswordField(15);
            add(newPasswordField, g(c, 1, 3, 1));

            // Confirm Password
            add(new JLabel("Confirm Password:"), g(c, 0, 4, 1));
            confirmPasswordField = new JPasswordField(15);
            add(confirmPasswordField, g(c, 1, 4, 1));

            // Update Password Button
            add(btn("Update Password", this::onUpdatePassword),
                    g(c, 0, 5, 2));


            add(btn("Back", () -> go("cust_profile")), g(c, 0, 6, 2));
        }

        private void onUpdatePassword() {
            String UserInput = new String(currentPasswordField.getPassword());
            if (UserInput.equals(currentUser.getPassword())) {
                String newPw = new String(newPasswordField.getPassword());
                String confirmPw = new String(confirmPasswordField.getPassword());


                UpdatePassword action = new UpdatePassword();
                action.setUser(currentUser);
                action.setNewPasssword(newPw);
                action.setConfirmationPassword(confirmPw);
                try {
                    action.prepare();
                } catch (InvalidAuthenticationException e) {
                    toast("No permission to update password!");
                    return;
                } catch (InvalidInputException e) {
                    toast("Password not identical, please input again!");
                    return;
                }
                try {
                    action.execute();
                } catch (InvalidAuthenticationException e) {
                    toast(e.getMessage());
                } catch (InvalidAccountException e) {
                    toast(e.getMessage());
                }
                toast("Password updated!");

            } else {
                toast("Invalid Current Password");
            }
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

            add(btn("View Customer Information", () -> go("teller_view_accounts")), g(c, 0, 1, 2));
            add(btn("View All Transactions", () -> go("teller_view_transactions")), g(c, 0, 2, 2));
            add(btn("Back", () -> go("teller")), g(c, 0, 3, 2));
        }
    }

    class TellerViewAccounts extends JPanel {
        private JComboBox<String> accountDropdown;
        private JLabel accountDetailsLabel;

        TellerViewAccounts() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("Customer Information"), g(c, 0, 0, 2));

            // Dropdown to select account
            add(new JLabel("Select Account:"), g(c, 0, 1, 1));
            accountDropdown = new JComboBox<>();
            add(accountDropdown, g(c, 1, 1, 1));

            // Display account details
            accountDetailsLabel = new JLabel("Select an account to view details");
            add(accountDetailsLabel, g(c, 0, 2, 2));

            // View button
            add(btn("View Details", this::displayAccountDetails), g(c, 0, 3, 2));
            add(btn("Back", () -> {
                // clear fields to avoid leaking data to another session of teller
                clearFields();
                go("teller_manage");
            }), g(c, 0, 4, 2));

            // Load accounts when panel is shown
            addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    loadAccountsInBranch();
                }
            });
        }

        private void clearFields() {
            accountDropdown.removeAllItems();
            accountDetailsLabel.setText("Select an account to view details");
        }

        private void loadAccountsInBranch() {
            accountDropdown.removeAllItems();
            try {
                String tellerBranchID = ((Teller) currentUser).getBranchID();
                ArrayList<Account> accounts = DatabaseSingleton.getDatabase()
                        .getAccountsByBranch(tellerBranchID);

                if (accounts.isEmpty()) {
                    toast("No accounts found in this branch.");
                    return;
                }

                for (Account account : accounts) {
                    String displayText = account.getAccountID() + " - " +
                            account.getCustomer().getFirstName() + " " +
                            account.getCustomer().getLastName() + " (" +
                            account.getClass().getSimpleName() + ")";
                    accountDropdown.addItem(displayText);
                }
            } catch (Exception e) {
                toast("Error loading accounts: " + e.getMessage());
            }
        }

        private void displayAccountDetails() {
            // Display selected account info
            if (accountDropdown.getSelectedItem() == null) {
                toast("Please select an account");
                return;
            }

            String selected = (String) accountDropdown.getSelectedItem();
            String accountID = selected.split(" - ")[0];

            try {
                Account account = DatabaseSingleton.getDatabase().getAccountByID(accountID);
                String details = "<html><b>Account ID: " + account.getAccountID() + "</b><br>" +
                        "Customer: " + account.getCustomer().getFirstName() + " "
                        + account.getCustomer().getLastName() + "<br>" +
                        "Account Type: " + account.getClass().getSimpleName() + "<br>" +
                        "Balance: $" + account.getBalance() + "</html>";
                accountDetailsLabel.setText(details);
            } catch (Exception e) {
                toast("Error retrieving account details: " + e.getMessage());
            }
        }
    }

    class TellerViewTransactions extends JPanel {
        final String titleText = "Branch Transactions";
        final BranchTransactionTable tableModel = new BranchTransactionTable();
        final JTable viewTable = new JTable(tableModel);
        protected ViewTransactionAction viewTransactionAction;

        TellerViewTransactions() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();

            // Title
            JLabel title = new JLabel(this.titleText);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
            add(title, g(c, 0, 0, 2));

            // Table
            viewTable.setFillsViewportHeight(true);
            viewTable.setAutoCreateRowSorter(true);

            JScrollPane scroll = new JScrollPane(viewTable);
            GridBagConstraints tableC = g(c, 0, 1, 2);
            tableC.weighty = 1;
            tableC.fill = GridBagConstraints.BOTH;
            add(scroll, tableC);

            //add(btn("Back", () -> go("teller_manage")), g(c, 0, 2, 2));

            // Footer Buttons
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton backBtn = new JButton("Back");
            backBtn.addActionListener(e -> go("teller_manage"));
            actions.add(backBtn);
            add(actions, g(c, 0, 2, 2));

            // Fetch trigger
            addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    loadBranchTransactions();
                }
            });
        }

        private void loadBranchTransactions() {
            try {
                String tellerBranchID = ((Teller) currentUser).getBranchID();
                Branch tellerBranch = DatabaseSingleton.getDatabase().getBranchByID(tellerBranchID);
                ArrayList<Account> accounts = DatabaseSingleton.getDatabase()
                        .getAccountsByBranch(tellerBranchID);
                List<Transaction> allTransactions = new ArrayList<>();

                for (Account account : accounts) {
                    viewTransactionAction = new ViewTransactionAction();
                    viewTransactionAction.setUser(currentUser);
                    viewTransactionAction.setAccountViewed(account);
                    viewTransactionAction.execute();

                    // Filter transactions - only add if account is in branch
                    for (Transaction t : viewTransactionAction.getListOfTransactions()) {
                        Account transactionAccount = t.getSender();
                        if (transactionAccount != null && tellerBranch.getAccountIds().contains(transactionAccount.getAccountID())) {
                            allTransactions.add(t);
                        }
                    }
                }

                tableModel.setTransactions(allTransactions);
            } catch (InvalidAuthenticationException | InvalidAccountException e) {
                toast("Error loading accounts: " + e.getMessage());
            }
        }

        class BranchTransactionTable extends AbstractTableModel {
            private final String[] columns = {"Transaction ID", "Sender ID", "Amount", "Time"};
            private List<Transaction> data = new ArrayList<>();

            public void setTransactions(List<Transaction> data) {
                this.data = data;
                fireTableDataChanged();
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
                    case 1 -> t.getSender().getAccountID();
                    case 2 -> t.getAmount();
                    case 3 -> t.getTimeOfTransaction();
                    default -> "";
                };
            }
        }
    }
    // ---------- Admin ----------

    // YO YO YO CHECK IT OUT, IT'S THE OMINIPOTENT ADMIN USER
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

            add(btn("Create Accounts", () -> go("admin_user_mgmt_create_account")), g(c, 0, 1, 2));
            add(btn("Reset Account Passwords", () -> go("admin_user_mgmt_update_password")), g(c, 0, 2, 2));
            add(btn("Deactivate Account", () -> go("admin_user_mgmt_deactivate_account")), g(c, 0, 3, 2));
            add(btn("View All Transactions", () -> go("admin_user_mgmt_view_transactions")), g(c, 0, 4, 2));
            add(btn("Back", () -> go("admin")), g(c, 0, 5, 2));
        }
    }

    class AdminUpdatePassword extends JPanel {
        private final UserComboBox userDropDown;
        private final JPasswordField newPasswordField;
        private final JPasswordField confirmPasswordField;
        private ArrayList<User> users = null;

        AdminUpdatePassword() {
            super(new GridBagLayout());
            setBorder(pad());

            GridBagConstraints c = gbc();

            // Title
            add(title("Update Password"), g(c, 0, 0, 2));

            // Email
            add(new JLabel("Select Account:"), g(c, 0, 1, 1));
            userDropDown = new UserComboBox();
            add(userDropDown, g(c, 1, 1, 1));

            // New Password
            add(new JLabel("New Password:"), g(c, 0, 3, 1));
            newPasswordField = new JPasswordField(15);
            add(newPasswordField, g(c, 1, 3, 1));

            // Confirm Password
            add(new JLabel("Confirm Password:"), g(c, 0, 4, 1));
            confirmPasswordField = new JPasswordField(15);
            add(confirmPasswordField, g(c, 1, 4, 1));

            // Update Password Button
            add(btn("Update Password", this::onUpdatePassword),
                    g(c, 0, 5, 2));

            // Back Button
            add(btn("Back", () -> go("admin")),
                    g(c, 0, 6, 2));


            addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    loadAccounts();
                }
            });
        }

        private void onUpdatePassword() {
            String newPw = new String(newPasswordField.getPassword());
            String confirmPw = new String(confirmPasswordField.getPassword());

            UpdatePassword action = new UpdatePassword();
            action.setUser(userDropDown.getSelectedUser());
            action.setNewPasssword(newPw);
            action.setConfirmationPassword(confirmPw);
            try {
                action.prepare();
            } catch (InvalidAuthenticationException e) {
                toast("No permission to update password!");
                return;
            } catch (InvalidInputException e) {
                toast("Password not identical, please input again!");
                return;
            }
            try {
                action.execute();
            } catch (InvalidAuthenticationException e) {
                toast(e.getMessage());
            } catch (InvalidAccountException e) {
                toast(e.getMessage());
            }
            toast("Password updated!");


        }

        private void loadAccounts() {
            users = DatabaseSingleton.getDatabase().getAllUsers();
            userDropDown.setUsers(users);
        }

        static class UserComboBox extends JComboBox<User> {
            private final DefaultComboBoxModel<User> model;

            public UserComboBox() {
                super();
                model = new DefaultComboBoxModel<>();
                setModel(model);

                setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                        if (value instanceof User u) {
                            setText(getDisplayText(u));
                        } else {
                            setText("");
                        }
                        return this;
                    }
                });
            }

            /**
             * Replace all users in the combo box.
             */
            public void setUsers(List<User> users) {
                model.removeAllElements();
                if (users == null) return;
                for (User u : users) {
                    model.addElement(u);
                }
                if (model.getSize() > 0) {
                    setSelectedIndex(0);
                }
            }

            /**
             * Returns the User currently selected, or null.
             */
            public User getSelectedUser() {
                Object sel = getSelectedItem();
                return (sel instanceof User) ? (User) sel : null;
            }

            /**
             * Helper to decide what text gets shown for each User in the dropdown.
             * Adjust this for your actual User fields.
             */
            private String getDisplayText(User u) {
                return u.getEmail();
            }

        }
    }


    class AdminCreateAccount extends JPanel {
        private JTextField passwordField;
        private JTextField emailField;
        private JComboBox<String> roleField;

        private JLabel accountTypeLabel;
        private JComboBox<String> accountTypeField;

        AdminCreateAccount() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();

            // Trigger update/reset whenever the panel becomes active
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    resetFields();    // ← refresh-ready hook
                }
            });

            add(title("Account Creation"), g(c, 0, 0, 2));

            // ----------------------------------------------------------------
            // FORM FIELDS (xyw)
            // ----------------------------------------------------------------
            // --- Email ---
            add(new JLabel("Email:"), g(c, 0, 1, 1));
            emailField = new JTextField(15);
            add(emailField, g(c, 1, 1, 1));

            // --- Password ---
            add(new JLabel("Password:"), g(c, 0, 2, 1));
            passwordField = new JPasswordField(15);   // or new JTextField(15) if you prefer
            add(passwordField, g(c, 1, 2, 1));

            // --- Role ---
            add(new JLabel("Role:"), g(c, 0, 3, 1));
            roleField = new JComboBox<>(new String[]{
                    Customer.class.getSimpleName(),
                    Teller.class.getSimpleName(),
                    Admin.class.getSimpleName()
            });
            roleField.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selected = (String) e.getItem();
                    boolean isCustomer = selected.equals(Customer.class.getSimpleName());
                    accountTypeLabel.setVisible(isCustomer);
                    accountTypeField.setVisible(isCustomer);
                    revalidate();
                    repaint();
                }
            });
            add(roleField, g(c, 1, 3, 1));

            // --- Account Type (only for Customer) ---
            accountTypeLabel = new JLabel("Account Type:");
            accountTypeField = new JComboBox<>(new String[]{
                    "Chequing",
                    "Saving",
                    "Card"
            });
            accountTypeLabel.setVisible(false);
            accountTypeField.setVisible(true);
            add(accountTypeLabel, g(c, 0, 4, 1));
            add(accountTypeField, g(c, 1, 4, 1));

            // --- Create Button ---
            add(btn("Create Account", this::onCreateAccount),
                    g(c, 0, 6, 2));

            // --- Back Button ---
            add(btn("Back", () -> go("admin")),
                    g(c, 0, 7, 2));

        }

        private void onCreateAccount() {
//          String firstName = firstNameField.getText().trim();
//          String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            String type = Objects.requireNonNull(roleField.getSelectedItem()).toString();

            CreateUserAction action = new CreateUserAction();
            action.setUser(currentUser);
            User newUser = switch (type) {
                case "Admin" -> new Admin();
                case "Customer" -> new Customer();
                case "Teller" -> new Teller();
                default -> throw new IllegalStateException("Unexpected value: " + roleField);
            };
            newUser.setEmail(email);
            newUser.setPassword(password);
            action.setNewUser(newUser);
            try {
                action.execute();
                toast(CreateUserAction.MESSAGE);
                resetFields(); // clean up after creation
            } catch (InvalidAuthenticationException e) {
                toast("No permission to create an user!");
            } catch (InvalidAccountException e) {
                toast("Duplicate email with another user!");
            }

            // Create account associate with Customer

            if (newUser instanceof Customer) {
                CreateAccountAction createAccountAction = new CreateAccountAction();
                createAccountAction.setUser(currentUser);
                createAccountAction.setAssociatedUser(newUser);
                createAccountAction.setAccountType(accountTypeField.getSelectedItem().toString());
                try {
                    createAccountAction.execute();
                } catch (InvalidAuthenticationException e) {
                    toast(e.getMessage());
                } catch (InvalidAccountException e) {
                    toast(e.getMessage());
                }
            }


        }

        private void resetFields() {
            passwordField.setText("");
            emailField.setText("");
        }
    }

    class AdminDeactivateAccount extends JPanel {
        private JTextField emailField;
        private JLabel emailLabel;
        private JLabel accountLabel;
        private final AccountsComboBox accountsDropDown = new AccountsComboBox();
        protected DeactivateAccountAction deactivateAccountAction;
        private ArrayList<Account> accounts;
        private Account selectedAccount;

        AdminDeactivateAccount() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("Deactivate Account"), g(c, 0, 0, 2));

            // UI components for this page:
            // - A text field for user input (the email of the account to deactivate)
            // - A dropdown to select accounts associated with that email (customer)
            // - A "Deactivate" button to perform the action
            emailField = new JTextField(22);
            emailLabel = new JLabel("Customer Email: ");
            add(emailLabel, g(c, 0, 1, 1));
            add(emailField, g(c, 1, 1, 1));
            add(btn("Search", () -> {
                String email = emailField.getText().trim();
                loadAccounts(email);
            }), g(c, 0, 2, 2));

            accountLabel = new JLabel("Select Account: ");
            add(accountLabel, g(c, 0, 3, 1));
            add(accountsDropDown, g(c, 1, 3, 1));

            add(btn("Deactivate Account", () -> {
                selectedAccount = accountsDropDown.getSelectedAccount();
                if (selectedAccount.getAccountStatus() == Account.AccountStatus.INACTIVE) {
                    toast("Account has already been deactivated.");
                    resetFields();
                    return;
                }
                if (selectedAccount == null) {
                    toast("No account selected!");
                    resetFields();
                    return;
                }
                deactivateAccountAction = new DeactivateAccountAction(currentUser, selectedAccount);
                try {
                    deactivateAccountAction.execute();
                    info("Account Deactivated!");
                } catch (InvalidAuthenticationException e) {
                    toast("No permission to deactivate account!");
                } catch (InvalidAccountException e) {
                    toast("Account deactivation failed!");
                }
                resetFields();
            }), g(c, 0, 4, 2));
            add(btn("Back", () -> {
                go("admin_user_mgmt");
                resetFields();
            }), g(c, 0, 5, 2));
        }

        private void loadAccounts(String email) {
            accounts = DatabaseSingleton.getDatabase().getAccountsByEmail(email);
            accountsDropDown.setAccounts(accounts);
            revalidate();
            repaint();
        }

        private void resetFields() {
            emailField.setText("");
            accountsDropDown.setAccounts(new ArrayList<>());
        }

        static class AccountsComboBox extends JComboBox<Account> {
            private final DefaultComboBoxModel<Account> model;

            public AccountsComboBox() {
                super();
                model = new DefaultComboBoxModel<>();
                setModel(model);

                setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                        if (value instanceof Account a) {
                            setText(getDisplayText(a));
                        } else {
                            setText("");
                        }
                        return this;
                    }
                });
            }

            /**
             * Replace all accounts in the combo box.
             */
            public void setAccounts(List<Account> accounts) {
                model.removeAllElements();
                if (accounts == null) return;
                for (Account a : accounts) {
                    model.addElement(a);
                }
                if (model.getSize() > 0) {
                    setSelectedIndex(0);
                }
            }

            /**
             * Returns the Account currently selected, or null.
             */
            public Account getSelectedAccount() {
                Object sel = getSelectedItem();
                return (sel instanceof Account) ? (Account) sel : null;
            }

            /**
             * Helper to decide what text gets shown for each Account in the dropdown.
             * Adjust this for your actual Account fields.
             */
            private String getDisplayText(Account a) {
                return a.getAccountID() + " - " +
                        a.getCustomer().getFirstName() + " " +
                        a.getCustomer().getLastName() + " (" +
                        a.getClass().getSimpleName() + ") - " + a.getAccountStatus();
            }
        }
    }

    class AdminViewTransactions extends JPanel {
        // The transactions to be displayed
        final TransactionTable tableModel = new TransactionTable();
        final JTable viewTable = new JTable(tableModel);
        protected ViewTransactionAction viewTransactionAction;

        AdminViewTransactions() {
            super(new GridBagLayout());
            setBorder(pad());
            GridBagConstraints c = gbc();
            add(title("All Transactions"), g(c, 0, 0, 2));

            // ----- Table -----
            viewTable.setFillsViewportHeight(true);
            viewTable.setAutoCreateRowSorter(true);

            JScrollPane scroll = new JScrollPane(viewTable);
            GridBagConstraints tableC = g(c, 0, 1, 2);
            tableC.weighty = 1;
            tableC.fill = GridBagConstraints.BOTH;
            add(scroll, tableC);

            add(btn("Back", () -> go("admin_user_mgmt")), g(c, 0, 2, 2));

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
            viewTransactionAction.setAccountViewed(null);
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

