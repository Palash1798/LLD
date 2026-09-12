package com.splitwise.splitwise.exceptions;

public class MemberNotInGroupException extends RuntimeException {

    public MemberNotInGroupException(String userId, String groupId) {
        super("User " + userId + " is not a member of group " + groupId);
    }
}
