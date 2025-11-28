package bank;


import java.util.ArrayList;
import java.util.List;
/**
 * Represents a bank, which contains multiple branches.
 * Each bank has a name and a list of its branches.
 */
public class Bank {
    /** Name of the bank. */
    private final String name;
    /** List of branches that belong to this bank. */
    private final List<Branch> branches;

    /**
     * Creates a new bank with the specified name.
     * Initializes an empty list of branches.
     *
     * @param name the name of the bank
     */
    public Bank(String name) {
        this.name = name;
        this.branches = new ArrayList<>();
    }

    /**
     * Returns the name of the bank.
     *
     * @return bank name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of branches in this bank.
     *
     * @return list of branches
     */
    public List<Branch> getBranches() {
        return branches;
    }

    /**
     * Adds a branch to this bank.
     *
     * @param branch the branch to add
     */
    public void addBranch(Branch branch) {
        branches.add(branch);
    }

}
