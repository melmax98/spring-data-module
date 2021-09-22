package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.service.UserService;
import org.example.storage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        Page<User> usersPage = userRepository.findByName(name, PageRequest.of(pageNum - 1, pageSize));
        return usersPage.getContent();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        User foundUser = userRepository.findById(user.getUserId()).orElseThrow(NullPointerException::new);
        foundUser.setName(user.getName());
        foundUser.setEmail(user.getEmail());
        return userRepository.save(foundUser);
    }

    @Override
    public boolean deleteUser(long userId) {
        try {
            userRepository.deleteById(userId);
            return true;
        } catch (Exception e) {
            log.error("Was not able to delete user with id {}", userId, e);
            return false;
        }
    }
}
