package org.example.storage.dao;

import org.example.model.Storable;
import org.example.model.User;
import org.example.storage.NoData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {
    @Mock
    private NoData noData;

    @InjectMocks
    private UserDao userDao;

    @Test
    void save() {
        when(noData.getStorage()).thenReturn(new HashMap<>());

        User user = new User();
        user.setName("New");
        user.setEmail("new@mail.com");

        User savedUser = userDao.save(user);

        assertEquals(1, savedUser.getUserId());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    void update_userExists() {
        HashMap<String, Storable> storage = new HashMap<>();
        User user = new User();
        user.setUserId(1L);
        user.setName("New");
        user.setEmail("new@mail.com");
        storage.put("user:1", user);

        when(noData.getStorage()).thenReturn(storage);

        user.setEmail("my@mail.com");
        user.setName("Jack");
        User updatedUser = userDao.update(user);

        assertEquals(1, updatedUser.getUserId());
        assertEquals("Jack", updatedUser.getName());
        assertEquals("my@mail.com", updatedUser.getEmail());
    }

    @Test
    void update_userDoesNotExist() {
        HashMap<String, Storable> storage = new HashMap<>();
        User user = new User();
        user.setUserId(1L);
        user.setName("New");
        user.setEmail("new@mail.com");

        when(noData.getStorage()).thenReturn(storage);

        user.setEmail("my@mail.com");
        user.setName("Jack");
        User updatedUser = userDao.update(user);

        assertNull(updatedUser);
    }

    @Test
    void delete_userExists() {
        HashMap<String, Storable> storage = new HashMap<>();
        User user = new User();
        user.setUserId(1L);
        user.setName("New");
        user.setEmail("new@mail.com");
        storage.put("user:1", user);

        when(noData.getStorage()).thenReturn(storage);

        boolean userDeleted = userDao.delete(1);

        assertTrue(userDeleted);
    }

    @Test
    void delete_userDoesNotExist() {
        when(noData.getStorage()).thenReturn(new HashMap<>());

        boolean userDeleted = userDao.delete(1);

        assertFalse(userDeleted);
    }

    @Test
    void getUsersByName() {
        HashMap<String, Storable> storage = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            User user = new User();
            user.setUserId(1L);
            user.setName("New");
            user.setEmail("new@mail.com");
            storage.put("user:" + i, user);
        }

        when(noData.getStorage()).thenReturn(storage);

        List<User> userList = userDao.getUsersByName("New", 5, 2);

        assertEquals(4, userList.size());
    }

    @Test
    void getUsersByName_notFound() {
        when(noData.getStorage()).thenReturn(new HashMap<>());

        List<User> userList = userDao.getUsersByName("New", 5, 1);

        assertTrue(userList.isEmpty());
    }

    @Test
    void getUserByEmail() {
        HashMap<String, Storable> storage = new HashMap<>();
        User user = new User();
        user.setUserId(1L);
        user.setName("New");
        user.setEmail("new@mail.com");
        storage.put("user:1", user);
        when(noData.getStorage()).thenReturn(storage);

        User foundUser = userDao.getUserByEmail("new@mail.com");

        assertEquals(user, foundUser);
    }
}