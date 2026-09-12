package com.splitwise.splitwise.exceptions;

public class GroupNotFoundException extends RuntimeException {

    public GroupNotFoundException(String groupId) {
        super("Group not found: " + groupId);
    }
}
