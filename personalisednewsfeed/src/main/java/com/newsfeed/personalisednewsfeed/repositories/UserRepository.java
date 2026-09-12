package com.newsfeed.personalisednewsfeed.repositories;

import com.newsfeed.personalisednewsfeed.exceptions.UserNotFoundException;
import com.newsfeed.personalisednewsfeed.models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory user store (interview substitute for a DB).
 *
 * Production swap: replace Map with JPA / JDBC — services stay unchanged (Repository pattern).
 */
public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public void save(User user) {
        users.put(user.getId(), user);
    }

    public User findById(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }

    public boolean exists(String userId) {
        return users.containsKey(userId);
    }
}
