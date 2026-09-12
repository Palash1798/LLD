package com.newsfeed.personalisednewsfeed.factories;

import com.newsfeed.personalisednewsfeed.controller.FeedController;
import com.newsfeed.personalisednewsfeed.models.User;
import com.newsfeed.personalisednewsfeed.repositories.PostRepository;
import com.newsfeed.personalisednewsfeed.repositories.UserRepository;
import com.newsfeed.personalisednewsfeed.services.FeedService;
import com.newsfeed.personalisednewsfeed.services.PostService;
import com.newsfeed.personalisednewsfeed.services.UserService;

/**
 * Wires all dependencies for the demo — same role as AccountFactory in ATM project.
 *
 * Step 0 in every interview: create repos → services → controller.
 */
public final class NewsFeedFactory {

    private NewsFeedFactory() {
    }

    /** Full stack with empty repositories. */
    public static FeedController createController() {
        UserRepository userRepository = new UserRepository();
        PostRepository postRepository = new PostRepository();

        UserService userService = new UserService(userRepository);
        PostService postService = new PostService(postRepository, userRepository);
        FeedService feedService = new FeedService(userRepository, postRepository);

        return new FeedController(userService, postService, feedService);
    }

    /**
     * Pre-seeded demo users: Alice, Bob, Carol.
     * Returns controller + user references for the scripted demo.
     */
    public static DemoContext createDemoContext() {
        FeedController controller = createController();

        User alice = controller.registerUser("Alice");
        User bob = controller.registerUser("Bob");
        User carol = controller.registerUser("Carol");

        return new DemoContext(controller, alice, bob, carol);
    }

    public record DemoContext(FeedController controller, User alice, User bob, User carol) {
    }
}
