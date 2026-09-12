package com.splitwise.splitwise;

import com.splitwise.splitwise.controller.SplitwiseController;
import com.splitwise.splitwise.dto.AddExpenseRequest;
import com.splitwise.splitwise.factories.DemoDataFactory;

import java.util.List;

/**
 * Entry point — scripted study demo (like PaymentgatewayApplication).
 *
 * Walks through the 3 MVP features from LLD_SPLITWISE.md:
 *   1) Register users + create group + add members
 *   2) Add expense with EQUAL / UNEQUAL split (Strategy pattern)
 *   3) Show balance sheets — who owes whom
 *
 * IDE: run this class (or demo.SplitwiseDemo for interactive menu).
 *
 * Study order (read code in this order):
 *   DemoDataFactory → SplitwiseController → ExpenseService → BalanceService
 *   → SplitStrategy / EqualSplitStrategy → models/User + BalanceSheet
 */
public class SplitwiseApplication {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  SPLITWISE LLD — STUDY DEMO");
        System.out.println("  Patterns: Strategy + Factory + Ledger Facade");
        System.out.println("========================================\n");

        printStudyMap();

        // Step 0: wire all components via factory
        SplitwiseController controller = DemoDataFactory.createController();

        // ------------------------------------------------------------------
        // FEATURE 1: Users + group setup
        // ------------------------------------------------------------------
        section("FEATURE 1 — Register users, create group, add members");
        safe(() -> {
            DemoDataFactory.seedUsersAndGroup(controller);
            System.out.println("Group: " + controller.getGroup(DemoDataFactory.GROUP_ID));
            System.out.println("Members: Alice (creator), Bob, Carol");
        });

        // ------------------------------------------------------------------
        // FEATURE 2: EQUAL split expense
        // ------------------------------------------------------------------
        section("FEATURE 2 — Add expense (EQUAL split)");
        safe(() -> {
            // Alice pays ₹900.00 (= 90000 paise) split equally among Alice, Bob, Carol
            // Expected: each owes ₹300 → Bob owes Alice 300, Carol owes Alice 300
            var expense = controller.addExpense(AddExpenseRequest.equal(
                    DemoDataFactory.GROUP_ID,
                    DemoDataFactory.ALICE_ID,
                    90_000,
                    "Breakfast",
                    List.of(DemoDataFactory.ALICE_ID, DemoDataFactory.BOB_ID, DemoDataFactory.CAROL_ID)
            ));
            System.out.println("Created: " + expense);
        });

        // ------------------------------------------------------------------
        // FEATURE 3: Balance sheets after equal split
        // ------------------------------------------------------------------
        section("FEATURE 3 — Balance sheets after EQUAL expense");
        safe(() -> controller.printAllBalanceSheets());

        // ------------------------------------------------------------------
        // FEATURE 2 (continued): UNEQUAL split
        // ------------------------------------------------------------------
        section("FEATURE 2 — Add expense (UNEQUAL split)");
        safe(() -> {
            // Bob pays ₹500.00: Alice ₹400, Bob ₹100
            // Expected: Alice owes Bob ₹400
            var expense = controller.addExpense(AddExpenseRequest.unequal(
                    DemoDataFactory.GROUP_ID,
                    DemoDataFactory.BOB_ID,
                    50_000,
                    "Lunch",
                    List.of(DemoDataFactory.ALICE_ID, DemoDataFactory.BOB_ID),
                    List.of(40_000, 10_000)
            ));
            System.out.println("Created: " + expense);
        });

        // ------------------------------------------------------------------
        // FEATURE 3: Final balances (netting example)
        // ------------------------------------------------------------------
        section("FEATURE 3 — Final balance sheets (check net Alice ↔ Bob = ₹100)");
        safe(() -> {
            controller.printAllBalanceSheets();
            System.out.println("\nExpected nets:");
            System.out.println("  Alice: owes Bob 10000 paise (₹100) net — Bob had owed 300, Alice owes 400");
            System.out.println("  Bob:   gets 10000 paise from Alice net");
            System.out.println("  Carol: owes Alice 30000 paise (₹300)");
        });

        // ------------------------------------------------------------------
        // FAIL PATH: Invalid UNEQUAL split (sums don't match)
        // ------------------------------------------------------------------
        section("FAIL PATH — Invalid UNEQUAL split (400 + 50 != 500)");
        safe(() -> controller.addExpense(AddExpenseRequest.unequal(
                DemoDataFactory.GROUP_ID,
                DemoDataFactory.BOB_ID,
                50_000,
                "Bad Lunch",
                List.of(DemoDataFactory.ALICE_ID, DemoDataFactory.BOB_ID),
                List.of(40_000, 5_000)
        )));

        // ------------------------------------------------------------------
        // FAIL PATH: Payer not in participants
        // ------------------------------------------------------------------
        section("FAIL PATH — Payer not in participant list");
        safe(() -> controller.addExpense(AddExpenseRequest.equal(
                DemoDataFactory.GROUP_ID,
                DemoDataFactory.ALICE_ID,
                10_000,
                "Solo mistake",
                List.of(DemoDataFactory.BOB_ID, DemoDataFactory.CAROL_ID)
        )));

        // ------------------------------------------------------------------
        // EXTENSION: PERCENTAGE split (fresh users to avoid noise)
        // ------------------------------------------------------------------
        section("EXTENSION — PERCENTAGE split on new group");
        safe(() -> {
            SplitwiseController pctController = DemoDataFactory.createController();
            pctController.registerUser("PX1", "Priya");
            pctController.registerUser("PX2", "Rahul");
            pctController.createGroup("GPCT", "Roommates", "PX1");
            pctController.addMember("GPCT", "PX2");

            // Priya pays ₹1000: Priya 60%, Rahul 40%
            pctController.addExpense(AddExpenseRequest.percentage(
                    "GPCT",
                    "PX1",
                    100_000,
                    "Rent",
                    List.of("PX1", "PX2"),
                    List.of(60, 40)
            ));
            pctController.printAllBalanceSheets();
        });

        System.out.println("\nDemo complete. Re-read services/ExpenseService.java and BalanceService.java — that is the heart of this LLD.");
        System.out.println("For hands-on practice, run: com.splitwise.splitwise.demo.SplitwiseDemo");
    }

    private static void printStudyMap() {
        System.out.println("Study map (file → step):");
        System.out.println("  enums/SplitType.java              → Step 1");
        System.out.println("  models/*                          → Step 2");
        System.out.println("  repositories/*                    → Step 3");
        System.out.println("  strategies/SplitStrategy.java     → Step 4 (Strategy pattern)");
        System.out.println("  factories/SplitStrategyFactory    → Step 5");
        System.out.println("  services/UserService, GroupService→ Step 6");
        System.out.println("  services/BalanceService.java      → Step 7 (ledger — memorize this)");
        System.out.println("  services/ExpenseService.java      → Step 8 (orchestrator)");
        System.out.println("  controller/SplitwiseController    → Step 9");
        System.out.println("  factories/DemoDataFactory         → Step 10");
        System.out.println("  Amounts are in paise (90000 = ₹900.00)\n");
    }

    private static void section(String title) {
        System.out.println("\n>>> " + title);
        System.out.println("------------------------------------------------");
    }

    private static void safe(Runnable action) {
        try {
            action.run();
        } catch (RuntimeException ex) {
            System.out.println("!! " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }
}
