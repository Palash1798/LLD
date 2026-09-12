package com.splitwise.splitwise.factories;

import com.splitwise.splitwise.controller.SplitwiseController;
import com.splitwise.splitwise.repositories.GroupRepository;
import com.splitwise.splitwise.repositories.UserRepository;
import com.splitwise.splitwise.services.BalanceService;
import com.splitwise.splitwise.services.ExpenseService;
import com.splitwise.splitwise.services.GroupService;
import com.splitwise.splitwise.services.UserService;

/**
 * Step 10 — Wires all components for demo / interview setup.
 *
 * Like Payment Gateway DemoDataFactory — returns a ready SplitwiseController.
 */
public final class DemoDataFactory {

    /** Fixed user ids for scripted demos (easy to reference in comments). */
    public static final String ALICE_ID = "U1001";
    public static final String BOB_ID = "U2001";
    public static final String CAROL_ID = "U3001";
    public static final String GROUP_ID = "G1001";

    private DemoDataFactory() {
    }

    public static SplitwiseController createController() {
        // Step 10a: repositories (in-memory)
        UserRepository userRepository = new UserRepository();
        GroupRepository groupRepository = new GroupRepository();

        // Step 10b: services
        UserService userService = new UserService(userRepository);
        GroupService groupService = new GroupService(groupRepository, userRepository);
        BalanceService balanceService = new BalanceService();
        SplitStrategyFactory splitStrategyFactory = new SplitStrategyFactory();
        ExpenseService expenseService = new ExpenseService(
                groupService,
                userService,
                balanceService,
                splitStrategyFactory
        );

        // Step 10c: thin controller
        return new SplitwiseController(userService, groupService, expenseService, balanceService);
    }

    /**
     * Seeds Alice, Bob, Carol + "Goa Trip" group for quick demo start.
     * Call this from SplitwiseApplication or SplitwiseDemo.
     */
    public static void seedUsersAndGroup(SplitwiseController controller) {
        controller.registerUser(ALICE_ID, "Alice");
        controller.registerUser(BOB_ID, "Bob");
        controller.registerUser(CAROL_ID, "Carol");

        controller.createGroup(GROUP_ID, "Goa Trip", ALICE_ID);
        controller.addMember(GROUP_ID, BOB_ID);
        controller.addMember(GROUP_ID, CAROL_ID);
    }
}
