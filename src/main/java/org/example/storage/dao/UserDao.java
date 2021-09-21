package org.example.storage.dao;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Storable;
import org.example.model.User;
import org.example.storage.NoData;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class UserDao implements Dao {

    @Setter
    private NoData noData;

    private static final String USER_TITLE = "user:";

    @Override
    public User save(Storable storable) {
        long lastUserId = getStorage().values()
                .stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .mapToLong(User::getUserId)
                .max()
                .orElse(0L);

        User user = (User) storable;
        user.setUserId(lastUserId + 1);
        String entityKey = USER_TITLE + user.getUserId();

        getStorage().put(entityKey, user);
        return user;
    }

    @Override
    public User update(Storable storable) {
        User user = getUserById(((User) storable).getUserId());

        if (user == null) {
            log.warn("Could not update the user with id {} because it does not exist", ((User) storable).getUserId());
            return null;
        }

        user.setUserId(user.getUserId());
        user.setName(user.getName());
        user.setEmail(user.getEmail());

        String entityKey = USER_TITLE + user.getUserId();
        getStorage().put(entityKey, user);

        return user;
    }

    @Override
    public boolean delete(long userId) {
        User user = getUserById(userId);

        if (user == null) {
            log.warn("Could not delete the user with id {} because it does not exist", userId);
            return false;
        }

        getStorage().remove(USER_TITLE + userId);
        return true;
    }

    @Override
    public Map<String, Storable> getStorage() {
        return noData.getStorage();
    }

    public User getUserById(long userId) {
        String key = getStorage().keySet()
                .stream()
                .filter((USER_TITLE + userId)::equals)
                .findFirst()
                .orElse(null);

        if (key == null) {
            log.warn("User with id {} not found", userId);
            return null;
        }
        return (User) getStorage().get(key);
    }

    public User getUserByEmail(String email) {
        LinkedList<User> matchingUsers = getStorage().values()
                .stream()
                .filter(User.class::isInstance)
                .filter(user -> email.equals(((User) user).getEmail()))
                .map(User.class::cast)
                .collect(Collectors.toCollection(LinkedList::new));

        return matchingUsers.getFirst();
    }

    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        List<Storable> matchingUsers = getStorage().values()
                .stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .filter(user -> name.equals(user.getName()))
                .sorted(Comparator.comparing(User::getUserId))
                .collect(Collectors.toList());

        if (matchingUsers.isEmpty()) {
            log.info("No users with name {} found", name);
            return Collections.emptyList();
        }

        return getUsersPage(pageSize, pageNum, matchingUsers);
    }

    private List<User> getUsersPage(int pageSize, int pageNum, List<Storable> matchingUsers) {
        List<Storable> page = getPage(matchingUsers, pageNum, pageSize);

        return page
                .stream()
                .map(User.class::cast)
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return getStorage().values()
                .stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .sorted(Comparator.comparing(User::getUserId))
                .collect(Collectors.toList());
    }
}
