package auth.core;

import bank.Account;
import bank.Chequing;
import database.DatabaseSingleton;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @org.junit.jupiter.api.Test
    void className() {
        User user = new Customer("mutian.wang@hotmail.com", "12345", "MuTian", "Wang");
        assertEquals("customer", user.getClass().getSimpleName().toLowerCase());
    }

    @org.junit.jupiter.api.Test
    void createUser() {
        Customer customer = new Customer("mutian.wang@hotmail.com", "12345", "Mutian", "Wang");
        Account account = new Chequing(customer);
        DatabaseSingleton.getDatabase().addAccount(account);

    }


}