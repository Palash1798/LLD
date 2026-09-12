package com.newsfeed.personalisednewsfeed.controller;

import com.newsfeed.personalisednewsfeed.models.FeedPage;
import com.newsfeed.personalisednewsfeed.models.Post;
import com.newsfeed.personalisednewsfeed.models.User;
import com.newsfeed.personalisednewsfeed.services.FeedService;
import com.newsfeed.personalisednewsfeed.services.PostService;
import com.newsfeed.personalisednewsfeed.services.UserService;

/**
 * Thin controller — delegates to services (same role as AtmController / MatchController).
 *
 * In interview: wire this from demo main or add @RestController later.
 * Keeps CLI and future REST API on the same entry points.
 */
public class FeedController {

    private final UserService userService;
    private final PostService postService;
    private final FeedService feedService;

    public FeedController(UserService userService, PostService postService, FeedService feedService) {
        this.userService = userService;
        this.postService = postService;
        this.feedService = feedService;
    }

    // --- User / graph ---

    public User registerUser(String name) {
        return userService.registerUser(name);
    }

    public void follow(String userId, String targetId) {
        userService.follow(userId, targetId);
    }

    public void unfollow(String userId, String targetId) {
        userService.unfollow(userId, targetId);
    }

    public void block(String userId, String blockedId) {
        userService.block(userId, blockedId);
    }

    // --- Posts ---

    public Post createPost(String authorId, String content) {
        return postService.createPost(authorId, content);
    }

    public void likePost(String userId, String postId) {
        postService.likePost(userId, postId);
    }

    // --- Feed ---

    public FeedPage getFeed(String userId, int limit, String cursor) {
        return feedService.getFeed(userId, limit, cursor);
    }

    /** Pretty-print feed for CLI demo. */
    public void displayFeed(String userId, int limit, String cursor) {
        FeedPage page = getFeed(userId, limit, cursor);
        if (page.getPosts().isEmpty()) {
            System.out.println("  (empty feed)");
            return;
        }
        int rank = 1;
        for (Post post : page.getPosts()) {
            System.out.printf("  #%d  [likes=%d] %s%n", rank++, post.getLikeCount(), post.getContent());
        }
        if (page.isHasMore()) {
            System.out.println("  ... more available (cursor=" + page.getNextCursor() + ")");
        }
    }
}
