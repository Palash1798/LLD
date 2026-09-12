package com.splitwise.splitwise.demo;

import com.splitwise.splitwise.controller.SplitwiseController;
import com.splitwise.splitwise.dto.AddExpenseRequest;
import com.splitwise.splitwise.enums.SplitType;
import com.splitwise.splitwise.factories.DemoDataFactory;
import com.splitwise.splitwise.models.Group;
import com.splitwise.splitwise.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive CLI demo — drive Splitwise yourself (like PaymentGatewayDemo).
 *
 * Menu options map 1:1 to interview MVP features.
 */
public class SplitwiseDemo {

    private final SplitwiseController controller;
    private final Scanner scanner = new Scanner(System.in);

    public SplitwiseDemo(SplitwiseController controller) {
        this.controller = controller;
    }

    public static void main(String[] args) {
        System.out.println("=== Splitwise Interactive Demo ===\n");
        SplitwiseController controller = DemoDataFactory.createController();
        new SplitwiseDemo(controller).run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                running = handleChoice(choice);
            } catch (RuntimeException ex) {
                System.out.println("!! " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }
            System.out.println();
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
        System.out.println("--- Menu ---");
        System.out.println(" 1. Seed demo users + group (Alice, Bob, Carol)");
        System.out.println(" 2. Register user");
        System.out.println(" 3. Create group");
        System.out.println(" 4. Add member to group");
        System.out.println(" 5. Add EQUAL expense");
        System.out.println(" 6. Add UNEQUAL expense");
        System.out.println(" 7. Print all balance sheets");
        System.out.println(" 8. Print one user's balance sheet");
        System.out.println(" 0. Exit");
        System.out.print("Choice: ");
    }

    private boolean handleChoice(String choice) {
        return switch (choice) {
            case "1" -> {
                DemoDataFactory.seedUsersAndGroup(controller);
                System.out.println("Seeded. Group id = " + DemoDataFactory.GROUP_ID);
                yield true;
            }
            case "2" -> {
                System.out.print("Name: ");
                User user = controller.registerUser(scanner.nextLine().trim());
                System.out.println("Created " + user.getId());
                yield true;
            }
            case "3" -> {
                System.out.print("Group name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Creator user id: ");
                Group group = controller.createGroup(name, scanner.nextLine().trim());
                System.out.println("Created group " + group.getId());
                yield true;
            }
            case "4" -> {
                System.out.print("Group id: ");
                String groupId = scanner.nextLine().trim();
                System.out.print("User id to add: ");
                controller.addMember(groupId, scanner.nextLine().trim());
                yield true;
            }
            case "5" -> {
                addExpense(SplitType.EQUAL);
                yield true;
            }
            case "6" -> {
                addExpense(SplitType.UNEQUAL);
                yield true;
            }
            case "7" -> {
                controller.printAllBalanceSheets();
                yield true;
            }
            case "8" -> {
                System.out.print("User id: ");
                controller.printBalanceSheet(scanner.nextLine().trim());
                yield true;
            }
            case "0" -> false;
            default -> {
                System.out.println("Unknown option.");
                yield true;
            }
        };
    }

    private void addExpense(SplitType splitType) {
        System.out.print("Group id: ");
        String groupId = scanner.nextLine().trim();
        System.out.print("Paid by user id: ");
        String paidBy = scanner.nextLine().trim();
        System.out.print("Amount (paise): ");
        int amount = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Description: ");
        String desc = scanner.nextLine().trim();
        System.out.print("Participant user ids (comma-separated): ");
        List<String> participants = parseCsv(scanner.nextLine());

        AddExpenseRequest request;
        if (splitType == SplitType.EQUAL) {
            request = AddExpenseRequest.equal(groupId, paidBy, amount, desc, participants);
        } else {
            System.out.print("Exact amounts per participant (paise, comma-separated): ");
            List<Integer> amounts = parseIntCsv(scanner.nextLine());
            request = AddExpenseRequest.unequal(groupId, paidBy, amount, desc, participants, amounts);
        }

        System.out.println("Created: " + controller.addExpense(request));
    }

    private List<String> parseCsv(String line) {
        List<String> result = new ArrayList<>();
        for (String part : line.split(",")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }

    private List<Integer> parseIntCsv(String line) {
        List<Integer> result = new ArrayList<>();
        for (String part : line.split(",")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                result.add(Integer.parseInt(trimmed));
            }
        }
        return result;
    }
}
