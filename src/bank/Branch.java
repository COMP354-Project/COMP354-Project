package bank;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
/**
 * Represents a branch of a bank.
 * Each branch has a unique ID, name, address, phone number, and a list of account IDs.
 */
public class Branch {
    /** Unique identifier for the branch. */
    private final String id;
    /** Address of the branch. */
    private String address;
    /** Name of the branch. */
    private String name;
    /** Phone number of the branch. */
    private String phone;
    /** List of account IDs associated with this branch. */
    private ArrayList<String> accountIds;

    /**
     * Creates a new branch with a generated unique ID.
     *
     * @param address the address of the branch
     * @param name the name of the branch
     * @param phone the phone number of the branch
     */
    public Branch(String address, String name, String phone) {
        this.id = UUID.randomUUID().toString();
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.accountIds = new ArrayList<>();
    }

    /**
     * Creates a branch with a specified ID.
     *
     * @param id the unique ID of the branch
     * @param address the address of the branch
     * @param name the name of the branch
     * @param phone the phone number of the branch
     */
    public Branch(String id, String address, String name, String phone) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.accountIds = new ArrayList<>();
    }

    /**
     * Returns the unique ID of the branch.
     *
     * @return branch ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the address of the branch.
     *
     * @return branch address
     */
    public String getAddress() {
        return address;
    }
    /**
     * Returns the name of the branch.
     *
     * @return branch name
     */
    public String getName() {
        return name;
    }
    /**
     * Returns the phone number of the branch.
     *
     * @return branch phone
     */
    public String getPhone() {
        return phone;
    }
    /**
     * Returns the list of account IDs associated with this branch.
     *
     * @return list of account IDs
     */
    public ArrayList<String> getAccountIds() { return accountIds; }
    /**
     * Sets the address of the branch.
     *
     * @param address new address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * Sets the name of the branch.
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Sets the phone number of the branch.
     *
     * @param phone new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * Sets the list of account IDs associated with this branch.
     *
     * @param accountIds new list of account IDs
     */
    public void setAccountIds(ArrayList<String> accountIds) { this.accountIds = accountIds; }
    /**
     * Checks equality based on the branch ID.
     *
     * @param o object to compare
     * @return true if IDs are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Branch branch = (Branch) o;
        return Objects.equals(id, branch.id);
    }
    /**
     * Returns a string representation of the branch including its ID, name, address, phone, and account count.
     *
     * @return string representation of the branch
     */
    @Override
    public String toString() {
        return "Branch ID: " + id + "\n" +
               "Name: " + name + "\n" +
               "Address: " + address + "\n" +
               "Phone: " + phone + "\n" +
                "Accounts: " + (accountIds != null ? accountIds.size() : 0) + "\n";
    }
}

