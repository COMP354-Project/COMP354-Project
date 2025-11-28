package auth.core;

/**
 * Teller is a user that can view anyone's account.
 */
public class Teller extends User {
    /** The ID of the branch this teller belongs to. */
    private String branchID;

    /**
     * Returns the branch ID of this teller.
     *
     * @return the branch ID
     */
    public String getBranchID() {
        return branchID;
    }

    /**
     * Sets the branch ID of this teller.
     *
     * @param branchID the branch ID to set
     */
    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }
}
