package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.model.UserAccount;
import org.example.service.UserAccountService;
import org.example.storage.repository.UserAccountRepository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public Double getBalanceByUser(User user) {
        UserAccount userAccount = userAccountRepository.findByUser(user).orElse(new UserAccount(user, 0.0));
        return userAccount.getBalance();
    }

    @Override
    @Transactional
    public UserAccount refillAccount(User user, Double amount) {
        UserAccount userAccount = userAccountRepository.findById(user.getUserId()).orElse(new UserAccount(user, 0.0));
        double newBalance = userAccount.getBalance() + amount;
        userAccount.setBalance(newBalance);
        return userAccountRepository.save(userAccount);
    }

    @Override
    @Transactional
    public Boolean withdrawMoneyFromAccount(User user, Double amount) {
        UserAccount userAccount = userAccountRepository.findByUser(user).orElseThrow(NullPointerException::new);
        double newBalance = userAccount.getBalance() - amount;
        if (newBalance >= 0) {
            userAccount.setBalance(newBalance);
            userAccountRepository.save(userAccount);
            return true;
        }
        log.warn("Was not able to withdraw money from account. Not enough money.");
        log.warn("Needed amount: {}. Current balance: {}", amount, userAccount.getBalance());
        return false;
    }

    @Override
    public Boolean deleteUserAccount(long userAccountId) {
        try {
            userAccountRepository.deleteById(userAccountId);
            return true;
        } catch (Exception e) {
            log.error("Was not able to delete user account with id {}", userAccountId, e);
            return false;
        }
    }
}
