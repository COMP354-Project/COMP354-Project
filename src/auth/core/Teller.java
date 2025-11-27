package auth.core;

/**
 * Teller is a user that can view anyone's account.
 */
public class Teller extends User {
    private String branchID;

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }
}
