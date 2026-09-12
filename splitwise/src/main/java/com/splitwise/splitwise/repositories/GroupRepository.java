package com.splitwise.splitwise.repositories;

import com.splitwise.splitwise.models.Group;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Step 3b — In-memory group store.
 */
public class GroupRepository {

    private final Map<String, Group> groupsById = new ConcurrentHashMap<>();

    public void save(Group group) {
        groupsById.put(group.getId(), group);
    }

    public Optional<Group> findById(String groupId) {
        return Optional.ofNullable(groupsById.get(groupId));
    }
}
