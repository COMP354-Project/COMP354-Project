package bank;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Branch {
    private final String id;
    private String address;
    private String name;
    private String phone;
    private ArrayList<String> accountIds;

    public Branch(String address, String name, String phone) {
        this.id = UUID.randomUUID().toString();
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.accountIds = new ArrayList<>();
    }

    public Branch(String id, String address, String name, String phone) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.accountIds = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public ArrayList<String> getAccountIds() { return accountIds; }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAccountIds(ArrayList<String> accountIds) { this.accountIds = accountIds; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Branch branch = (Branch) o;
        return Objects.equals(id, branch.id);
    }

    @Override
    public String toString() {
        return "Branch ID: " + id + "\n" +
               "Name: " + name + "\n" +
               "Address: " + address + "\n" +
               "Phone: " + phone + "\n" +
                "Accounts: " + (accountIds != null ? accountIds.size() : 0) + "\n";
    }
}

