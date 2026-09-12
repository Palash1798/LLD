package com.splitwise.splitwise.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Step 2f — A group of users sharing expenses (e.g. "Goa Trip").
 *
 * Holds members and expense history for the group.
 */
public class Group {

    private final String id;
    private final String name;
    private final User createdBy;
    private final List<User> members = new ArrayList<>();
    private final List<Expense> expenses = new ArrayList<>();

    public Group(String id, String name, User createdBy) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.members.add(createdBy);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public List<User> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(expenses);
    }

    public void addMember(User user) {
        if (members.stream().noneMatch(m -> m.getId().equals(user.getId()))) {
            members.add(user);
        }
    }

    public boolean isMember(String userId) {
        return members.stream().anyMatch(m -> m.getId().equals(userId));
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    @Override
    public String toString() {
        return "Group{id='" + id + "', name='" + name + "', members=" + members.size()
                + ", expenses=" + expenses.size() + "}";
    }
}
