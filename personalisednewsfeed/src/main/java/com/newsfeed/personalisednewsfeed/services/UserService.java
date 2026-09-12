package com.newsfeed.personalisednewsfeed.services;

import com.newsfeed.personalisednewsfeed.exceptions.InvalidOperationException;
import com.newsfeed.personalisednewsfeed.models.User;
import com.newsfeed.personalisednewsfeed.repositories.UserRepository;

/**
 * Manages users and the social graph (follow / unfollow / block).
 *
 * FEATURE 1 (MVP): follow / unfollow
 */
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // -------------------------------------------------------------------------
    // User lifecycle
    // -------------------------------------------------------------------------

    public User registerUser(String name) {
        User user = new User(name);
        userRepository.save(user);
        System.out.println("[UserService] Registered: " + user);
        return user;
    }

    public User getUser(String userId) {
        return userRepository.findById(userId);
    }

    // -------------------------------------------------------------------------
    // FEATURE 1: Follow graph
    // -------------------------------------------------------------------------

    /**
     * Step 1: Alice follows Bob → Bob's posts become candidates in Alice's feed.
     *
     * Maintains bidirectional edges:
     *   Alice.following += Bob
     *   Bob.followers   += Alice   (for fan-out-on-write extension)
     */
    public void follow(String userId, String targetId) {
        if (userId.equals(targetId)) {
            throw new InvalidOperationException("Cannot follow yourself");
        }

        User user = userRepository.findById(userId);
        User target = userRepository.findById(targetId);

        // Idempotent — Set.add returns false if already following
        boolean added = user.getFollowing().add(targetId);
        if (added) {
            target.getFollowers().add(userId);
            System.out.println("[UserService] " + user.getName() + " now follows " + target.getName());
        } else {
            System.out.println("[UserService] " + user.getName() + " already follows " + target.getName());
        }
    }

    public void unfollow(String userId, String targetId) {
        User user = userRepository.findById(userId);
        User target = userRepository.findById(targetId);

        boolean removed = user.getFollowing().remove(targetId);
        if (removed) {
            target.getFollowers().remove(userId);
            System.out.println("[UserService] " + user.getName() + " unfollowed " + target.getName());
        }
    }

    /**
     * Block also unfollows — common product behavior.
     */
    public void block(String userId, String blockedId) {
        User user = userRepository.findById(userId);
        user.getBlockedUsers().add(blockedId);
        unfollow(userId, blockedId);
        System.out.println("[UserService] " + user.getName() + " blocked user " + blockedId);
    }
}
