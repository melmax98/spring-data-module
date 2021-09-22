package org.example.service;

import org.example.model.User;
import org.example.model.UserAccount;

public interface UserAccountService {
    Double getBalanceByUser(User user);

    UserAccount refillAccount(User user, Double amount);

    Boolean withdrawMoneyFromAccount(User user, Double amount);

    Boolean deleteUserAccount(long userAccountId);
}
