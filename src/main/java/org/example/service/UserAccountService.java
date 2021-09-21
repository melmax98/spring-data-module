package org.example.service;

import org.example.model.User;

public interface UserAccountService {
    Double getBalanceByUser(User user);

    Boolean refillAccount(User user, Double amount);

    Boolean withdrawMoneyFromAccount(User user, Double amount);
}
