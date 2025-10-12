package interfaces;

import bank.Account;

public interface Authentification {
    void authorize(User user, Account account);
    void isAuthorized();
}
