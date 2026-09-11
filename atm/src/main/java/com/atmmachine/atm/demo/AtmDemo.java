package com.atmmachine.atm.demo;

import com.atmmachine.atm.controller.AtmController;
import com.atmmachine.atm.factories.AccountFactory;
import com.atmmachine.atm.models.ATM;
import com.atmmachine.atm.models.CashDispenser;
import com.atmmachine.atm.services.BankingService;

import java.util.Scanner;

/**
 * Interactive CLI for hands-on practice.
 *
 * Use this when you want to drive the ATM yourself step-by-step
 * instead of watching the scripted AtmApplication demo.
 */
public class AtmDemo {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  ATM MACHINE — INTERACTIVE DEMO");
        System.out.println("========================================");
        System.out.println("Demo cards:");
        System.out.println("  4111111111111111 / 1234 (Alice)");
        System.out.println("  4222222222222222 / 5678 (Bob)");
        System.out.println("  4333333333333333 / 9999 (Carol)");
        System.out.println();

        BankingService bank = AccountFactory.createBankingService();
        CashDispenser dispenser = new CashDispenser(50_000);
        ATM atm = new ATM(bank, dispenser);
        AtmController controller = new AtmController();

        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            printMenu(atm);
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Card number: ");
                        controller.insertCard(atm, scanner.nextLine().trim());
                    }
                    case "2" -> {
                        System.out.print("PIN: ");
                        controller.enterPin(atm, scanner.nextLine().trim());
                    }
                    case "3" -> controller.checkBalance(atm);
                    case "4" -> {
                        System.out.print("Amount to withdraw: ");
                        int amount = Integer.parseInt(scanner.nextLine().trim());
                        controller.withdraw(atm, amount);
                    }
                    case "5" -> controller.ejectCard(atm);
                    case "6" -> controller.displayHistory(atm);
                    case "7" -> running = false;
                    default -> System.out.println("Invalid choice. Pick 1-7.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("!! Invalid number format.");
            } catch (RuntimeException ex) {
                System.out.println("!! " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }
            System.out.println();
        }

        scanner.close();
        System.out.println("Goodbye.");
    }

    private static void printMenu(ATM atm) {
        System.out.println("--- Menu (status=" + atm.getStatus() + ") ---");
        System.out.println("1. Insert card");
        System.out.println("2. Enter PIN");
        System.out.println("3. Check balance");
        System.out.println("4. Withdraw cash");
        System.out.println("5. Eject card");
        System.out.println("6. Show transaction history");
        System.out.println("7. Exit");
    }
}
