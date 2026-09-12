package com.splitwise.splitwise.controller;

import com.splitwise.splitwise.dto.AddExpenseRequest;
import com.splitwise.splitwise.models.Expense;
import com.splitwise.splitwise.models.Group;
import com.splitwise.splitwise.models.User;
import com.splitwise.splitwise.services.BalanceService;
import com.splitwise.splitwise.services.ExpenseService;
import com.splitwise.splitwise.services.GroupService;
import com.splitwise.splitwise.services.UserService;

import java.util.List;

/**
 * Step 9 — Thin controller layer (CLI today, @RestController tomorrow).
 *
 * Same role as PaymentController / AtmController — no business logic here.
 */
public class SplitwiseController {

    private final UserService userService;
    private final GroupService groupService;
    private final ExpenseService expenseService;
    private final BalanceService balanceService;

    public SplitwiseController(UserService userService,
                               GroupService groupService,
                               ExpenseService expenseService,
                               BalanceService balanceService) {
        this.userService = userService;
        this.groupService = groupService;
        this.expenseService = expenseService;
        this.balanceService = balanceService;
    }

    // -------------------------------------------------------------------------
    // MVP Feature 1: Users + groups
    // -------------------------------------------------------------------------

    public User registerUser(String name) {
        return userService.registerUser(name);
    }

    public User registerUser(String id, String name) {
        return userService.registerUser(id, name);
    }

    public Group createGroup(String name, String creatorUserId) {
        return groupService.createGroup(name, creatorUserId);
    }

    public Group createGroup(String groupId, String name, String creatorUserId) {
        return groupService.createGroup(groupId, name, creatorUserId);
    }

    public void addMember(String groupId, String userId) {
        groupService.addMember(groupId, userId);
    }

    // -------------------------------------------------------------------------
    // MVP Feature 2: Add expense
    // -------------------------------------------------------------------------

    public Expense addExpense(AddExpenseRequest request) {
        return expenseService.addExpense(request);
    }

    // -------------------------------------------------------------------------
    // MVP Feature 3: Show balances
    // -------------------------------------------------------------------------

    public void printBalanceSheet(String userId) {
        User user = userService.getUser(userId);
        balanceService.printBalanceSheet(user, userService);
    }

    public void printAllBalanceSheets() {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            balanceService.printBalanceSheet(user, userService);
        }
    }

    public User getUser(String userId) {
        return userService.getUser(userId);
    }

    public Group getGroup(String groupId) {
        return groupService.getGroup(groupId);
    }
}
