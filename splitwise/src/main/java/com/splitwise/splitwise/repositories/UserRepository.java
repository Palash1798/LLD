package com.splitwise.splitwise.repositories;

import com.splitwise.splitwise.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Step 3a — In-memory user store.
 *
 * Swap for JPA/JDBC in production; interface stays the same in a real system.
 */
public class UserRepository {

    private final Map<String, User> usersById = new ConcurrentHashMap<>();

    public void save(User user) {
        usersById.put(user.getId(), user);
    }

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(usersById.get(userId));
    }

    public List<User> findAll() {
        return new ArrayList<>(usersById.values());
    }
}
