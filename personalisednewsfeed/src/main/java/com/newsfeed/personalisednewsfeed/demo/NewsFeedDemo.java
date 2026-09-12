package com.newsfeed.personalisednewsfeed.demo;

import com.newsfeed.personalisednewsfeed.controller.FeedController;
import com.newsfeed.personalisednewsfeed.factories.NewsFeedFactory;
import com.newsfeed.personalisednewsfeed.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Interactive CLI for hands-on practice.
 *
 * Use this when you want to drive the news feed yourself step-by-step
 * instead of watching the scripted PersonalisednewsfeedApplication demo.
 */
public class NewsFeedDemo {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  NEWS FEED — INTERACTIVE DEMO");
        System.out.println("========================================");

        FeedController controller = NewsFeedFactory.createController();
        Map<String, User> usersByName = new HashMap<>();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("User name: ");
                        User user = controller.registerUser(scanner.nextLine().trim());
                        usersByName.put(user.getName().toLowerCase(), user);
                        System.out.println("Registered id=" + user.getId());
                    }
                    case "2" -> {
                        User from = resolveUser(scanner, usersByName, "Follower name");
                        User to = resolveUser(scanner, usersByName, "Target name");
                        controller.follow(from.getId(), to.getId());
                    }
                    case "3" -> {
                        User from = resolveUser(scanner, usersByName, "User name");
                        User to = resolveUser(scanner, usersByName, "Unfollow target");
                        controller.unfollow(from.getId(), to.getId());
                    }
                    case "4" -> {
                        User author = resolveUser(scanner, usersByName, "Author name");
                        System.out.print("Post content: ");
                        controller.createPost(author.getId(), scanner.nextLine().trim());
                    }
                    case "5" -> {
                        User viewer = resolveUser(scanner, usersByName, "Viewer name");
                        controller.displayFeed(viewer.getId(), 10, null);
                    }
                    case "6" -> {
                        System.out.print("User id (liker): ");
                        String userId = scanner.nextLine().trim();
                        System.out.print("Post id: ");
                        String postId = scanner.nextLine().trim();
                        controller.likePost(userId, postId);
                    }
                    case "7" -> running = false;
                    default -> System.out.println("Invalid choice. Pick 1-7.");
                }
            } catch (RuntimeException ex) {
                System.out.println("!! " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }
            System.out.println();
        }

        scanner.close();
        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println("--- Menu ---");
        System.out.println("1. Register user");
        System.out.println("2. Follow user");
        System.out.println("3. Unfollow user");
        System.out.println("4. Create post");
        System.out.println("5. View feed");
        System.out.println("6. Like post (by id)");
        System.out.println("7. Exit");
    }

    private static User resolveUser(Scanner scanner, Map<String, User> usersByName, String prompt) {
        System.out.print(prompt + ": ");
        String name = scanner.nextLine().trim();
        User user = usersByName.get(name.toLowerCase());
        if (user == null) {
            throw new IllegalArgumentException("Unknown user '" + name + "'. Register first (option 1).");
        }
        return user;
    }
}
