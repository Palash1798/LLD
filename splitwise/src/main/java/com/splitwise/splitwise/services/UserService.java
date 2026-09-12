package com.splitwise.splitwise.services;

import com.splitwise.splitwise.exceptions.UserNotFoundException;
import com.splitwise.splitwise.models.User;
import com.splitwise.splitwise.repositories.UserRepository;

import java.util.List;
import java.util.UUID;

/**
 * Step 6a — User registration and lookup.
 */
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Register a new user; generates id if not provided externally. */
    public User registerUser(String name) {
        String id = "U-" + UUID.randomUUID().toString().substring(0, 8);
        User user = new User(id, name);
        userRepository.save(user);
        System.out.println("[UserService] Registered " + user);
        return user;
    }

    /** Register with a fixed id (useful for scripted demos). */
    public User registerUser(String id, String name) {
        User user = new User(id, name);
        userRepository.save(user);
        System.out.println("[UserService] Registered " + user);
        return user;
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
