package com.newsfeed.personalisednewsfeed.models;

import java.util.HashSet;
import java.util.Set;

/**
 * A user in the social graph.
 *
 * Interview focus:
 *   - following  → who this user sees in their feed (fan-out source list)
 *   - followers  → reverse index (optional in MVP; needed for fan-out-on-write)
 *   - blockedUsers → filtered out before ranking
 */
public class User extends BaseModel {

    private final String name;
    private final Set<String> following = new HashSet<>();
    private final Set<String> followers = new HashSet<>();
    private final Set<String> blockedUsers = new HashSet<>();

    public User(String name) {
        super();
        this.name = name;
    }

    public User(String id, long createdAt, String name) {
        super(id, createdAt);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /** Users whose posts appear in this user's feed. */
    public Set<String> getFollowing() {
        return following;
    }

    /** Users who follow this user (reverse edge — for push fan-out extension). */
    public Set<String> getFollowers() {
        return followers;
    }

    /** Authors whose posts are hidden from this user's feed. */
    public Set<String> getBlockedUsers() {
        return blockedUsers;
    }

    @Override
    public String toString() {
        return "User{" + getId().substring(0, 8) + "..., name='" + name
                + "', following=" + following.size() + "}";
    }
}
