package com.paymentgateway.paymentgateway.demo;

import com.paymentgateway.paymentgateway.controller.PaymentController;
import com.paymentgateway.paymentgateway.dto.CreateOrderRequest;
import com.paymentgateway.paymentgateway.dto.CreateOrderResponse;
import com.paymentgateway.paymentgateway.dto.ProcessPaymentRequest;
import com.paymentgateway.paymentgateway.enums.PaymentMethod;
import com.paymentgateway.paymentgateway.factories.DemoDataFactory;
import com.paymentgateway.paymentgateway.models.PaymentInstrumentDetails;

import java.util.Scanner;
import java.util.UUID;

/**
 * Interactive CLI for hands-on practice.
 *
 * Use this when you want to drive the payment gateway yourself step-by-step
 * instead of watching the scripted PaymentgatewayApplication demo.
 */
public class PaymentGatewayDemo {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  PAYMENT GATEWAY — INTERACTIVE DEMO");
        System.out.println("========================================");
        System.out.println("Merchant: " + DemoDataFactory.MERCHANT_ID);
        System.out.println("API Key:  " + DemoDataFactory.API_KEY);
        System.out.println();

        PaymentController controller = DemoDataFactory.createPaymentController();
        Scanner scanner = new Scanner(System.in);

        String lastOrderId = null;
        boolean running = true;

        while (running) {
            printMenu(lastOrderId);
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Amount in paise: ");
                        int amount = Integer.parseInt(scanner.nextLine().trim());
                        String idempotencyKey = "cli-" + UUID.randomUUID();
                        CreateOrderResponse order = controller.createOrder(new CreateOrderRequest(
                                DemoDataFactory.MERCHANT_ID,
                                DemoDataFactory.API_KEY,
                                amount,
                                idempotencyKey,
                                "CLI-ORDER"
                        ));
                        lastOrderId = order.getOrderId();
                        System.out.println("Created: " + order);
                    }
                    case "2" -> {
                        String orderId = promptOrderId(scanner, lastOrderId);
                        PaymentMethod method = promptPaymentMethod(scanner);
                        PaymentInstrumentDetails details = promptInstrument(scanner, method);
                        System.out.println(controller.processPayment(new ProcessPaymentRequest(orderId, method, details)));
                        lastOrderId = orderId;
                    }
                    case "3" -> {
                        String orderId = promptOrderId(scanner, lastOrderId);
                        System.out.println(controller.getStatus(orderId));
                        lastOrderId = orderId;
                    }
                    case "4" -> {
                        String orderId = promptOrderId(scanner, lastOrderId);
                        System.out.println(controller.refund(orderId));
                        lastOrderId = orderId;
                    }
                    case "5" -> controller.displayOrderHistory(DemoDataFactory.MERCHANT_ID, DemoDataFactory.API_KEY);
                    case "6" -> running = false;
                    default -> System.out.println("Invalid choice. Pick 1-6.");
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

    private static void printMenu(String lastOrderId) {
        System.out.println("--- Menu" + (lastOrderId != null ? " (last order=" + lastOrderId + ")" : "") + " ---");
        System.out.println("1. Create order");
        System.out.println("2. Process payment");
        System.out.println("3. Get status");
        System.out.println("4. Refund");
        System.out.println("5. Merchant order history");
        System.out.println("6. Exit");
    }

    private static String promptOrderId(Scanner scanner, String lastOrderId) {
        if (lastOrderId != null) {
            System.out.print("Order ID [" + lastOrderId + "]: ");
            String input = scanner.nextLine().trim();
            return input.isEmpty() ? lastOrderId : input;
        }
        System.out.print("Order ID: ");
        return scanner.nextLine().trim();
    }

    private static PaymentMethod promptPaymentMethod(Scanner scanner) {
        System.out.println("Payment method: 1=UPI  2=CARD  3=WALLET");
        System.out.print("Choice: ");
        return switch (scanner.nextLine().trim()) {
            case "2" -> PaymentMethod.CARD;
            case "3" -> PaymentMethod.WALLET;
            default -> PaymentMethod.UPI;
        };
    }

    private static PaymentInstrumentDetails promptInstrument(Scanner scanner, PaymentMethod method) {
        return switch (method) {
            case CARD -> {
                System.out.print("Card number: ");
                String card = scanner.nextLine().trim();
                System.out.print("CVV: ");
                String cvv = scanner.nextLine().trim();
                System.out.print("Simulate failure? (y/n): ");
                if ("y".equalsIgnoreCase(scanner.nextLine().trim())) {
                    yield PaymentInstrumentDetails.failingCard(card, cvv);
                }
                yield PaymentInstrumentDetails.card(card, cvv);
            }
            case WALLET -> {
                System.out.print("Wallet ID: ");
                yield PaymentInstrumentDetails.wallet(scanner.nextLine().trim());
            }
            default -> {
                System.out.print("UPI ID: ");
                yield PaymentInstrumentDetails.upi(scanner.nextLine().trim());
            }
        };
    }
}
