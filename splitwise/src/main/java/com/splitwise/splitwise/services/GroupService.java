package com.splitwise.splitwise.services;

import com.splitwise.splitwise.exceptions.GroupNotFoundException;
import com.splitwise.splitwise.exceptions.UserNotFoundException;
import com.splitwise.splitwise.models.Group;
import com.splitwise.splitwise.models.User;
import com.splitwise.splitwise.repositories.GroupRepository;
import com.splitwise.splitwise.repositories.UserRepository;

import java.util.UUID;

/**
 * Step 6b — Group lifecycle: create, add members.
 */
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public Group createGroup(String name, String creatorUserId) {
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new UserNotFoundException(creatorUserId));

        String groupId = "G-" + UUID.randomUUID().toString().substring(0, 8);
        Group group = new Group(groupId, name, creator);
        groupRepository.save(group);
        System.out.println("[GroupService] Created " + group + " (creator auto-added as member)");
        return group;
    }

    public Group createGroup(String groupId, String name, String creatorUserId) {
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new UserNotFoundException(creatorUserId));

        Group group = new Group(groupId, name, creator);
        groupRepository.save(group);
        System.out.println("[GroupService] Created " + group);
        return group;
    }

    public void addMember(String groupId, String userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        group.addMember(user);
        System.out.println("[GroupService] Added " + user.getName() + " to group " + group.getName());
    }

    public Group getGroup(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
    }
}
