package com.newsfeed.personalisednewsfeed;

import com.newsfeed.personalisednewsfeed.controller.FeedController;
import com.newsfeed.personalisednewsfeed.factories.NewsFeedFactory;
import com.newsfeed.personalisednewsfeed.models.FeedPage;
import com.newsfeed.personalisednewsfeed.models.Post;
import com.newsfeed.personalisednewsfeed.models.User;

/**
 * Entry point — scripted study demo (no Spring required).
 *
 * Walks through the 3 MVP features from LLD_PERSONALISED_NEWS_FEED.md:
 *   1) Follow / unfollow user
 *   2) Create post
 *   3) Get personalized feed (Strategy ranking + Filter chain)
 *
 * IDE: run this class (or NewsFeedDemo for interactive menu).
 *
 * Maven: .\mvnw.cmd -q -DskipTests compile exec:java
 */
public class PersonalisednewsfeedApplication {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  PERSONALIZED NEWS FEED - STUDY DEMO");
        System.out.println("  Patterns: Strategy (ranking) + Chain (filters)");
        System.out.println("========================================\n");

        // Step 0: wire objects via factory (same as ATM AccountFactory pattern)
        NewsFeedFactory.DemoContext ctx = NewsFeedFactory.createDemoContext();
        FeedController controller = ctx.controller();
        User alice = ctx.alice();
        User bob = ctx.bob();
        User carol = ctx.carol();

        printUsers(alice, bob, carol);

        // ------------------------------------------------------------------
        // FEATURE 1: Follow / unfollow — build the social graph
        // ------------------------------------------------------------------
        section("FEATURE 1 — Follow / unfollow");
        controller.follow(alice.getId(), bob.getId());
        controller.follow(alice.getId(), carol.getId());
        System.out.println("Alice follows: Bob, Carol");

        // ------------------------------------------------------------------
        // FEATURE 2: Create posts
        // ------------------------------------------------------------------
        section("FEATURE 2 — Create posts");
        Post bobPost = controller.createPost(bob.getId(), "Launch day! Our product is live.");
        Post carolPost = controller.createPost(carol.getId(), "Morning coffee and coding.");

        // Give Carol's post more engagement so ranking demo is visible
        controller.likePost(alice.getId(), carolPost.getId());
        controller.likePost(bob.getId(), carolPost.getId());
        controller.likePost(alice.getId(), carolPost.getId()); // 3 likes total

        // Small delay so recency scores differ slightly (optional visual)
        sleep(10);
        Post bobPost2 = controller.createPost(bob.getId(), "Thanks everyone for the support!");

        // ------------------------------------------------------------------
        // FEATURE 3: Get personalized feed — full pipeline
        // ------------------------------------------------------------------
        section("FEATURE 3 — Alice's personalized feed (engagement ranking)");
        System.out.println("Expected order: Carol's post first (3 likes), then Bob's posts");
        controller.displayFeed(alice.getId(), 10, null);

        // ------------------------------------------------------------------
        // Pagination demo
        // ------------------------------------------------------------------
        section("PAGINATION — page size 2");
        FeedPage page1 = controller.getFeed(alice.getId(), 2, null);
        printPage(page1, 1);
        if (page1.isHasMore()) {
            FeedPage page2 = controller.getFeed(alice.getId(), 2, page1.getNextCursor());
            printPage(page2, 2);
        }

        // ------------------------------------------------------------------
        // Unfollow changes feed
        // ------------------------------------------------------------------
        section("UNFOLLOW — Alice unfollows Carol");
        controller.unfollow(alice.getId(), carol.getId());
        controller.displayFeed(alice.getId(), 10, null);
        System.out.println("Carol's posts should be gone — only Bob remains");

        // ------------------------------------------------------------------
        // Fail paths
        // ------------------------------------------------------------------
        section("FAIL PATH — Self follow");
        safe(() -> controller.follow(alice.getId(), alice.getId()));

        section("FAIL PATH — Unknown user");
        safe(() -> controller.getFeed("non-existent-id", 10, null));

        section("FAIL PATH — Like unknown post");
        safe(() -> controller.likePost(alice.getId(), "bad-post-id"));

        // ------------------------------------------------------------------
        // Block filter demo
        // ------------------------------------------------------------------
        section("FILTER — Alice re-follows Carol, then blocks her");
        controller.follow(alice.getId(), carol.getId());
        controller.createPost(carol.getId(), "This should NOT appear — Carol is blocked next");
        controller.block(alice.getId(), carol.getId());
        controller.displayFeed(alice.getId(), 10, null);
        System.out.println("BlockedUsersFilter removed Carol's posts even though Alice had followed her");

        System.out.println("\nDemo complete. Re-read FeedService.getFeed — that is the heart of this LLD.");
        System.out.println("For hands-on practice, run: com.newsfeed.personalisednewsfeed.demo.NewsFeedDemo");
    }

    private static void printUsers(User alice, User bob, User carol) {
        System.out.println("Demo users (from NewsFeedFactory):");
        System.out.println("  Alice  id=" + alice.getId());
        System.out.println("  Bob    id=" + bob.getId());
        System.out.println("  Carol  id=" + carol.getId());
        System.out.println();
    }

    private static void printPage(FeedPage page, int pageNum) {
        System.out.println("Page " + pageNum + ":");
        int rank = 1;
        for (Post post : page.getPosts()) {
            System.out.printf("  #%d  [likes=%d] %s%n", rank++, post.getLikeCount(), post.getContent());
        }
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

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
