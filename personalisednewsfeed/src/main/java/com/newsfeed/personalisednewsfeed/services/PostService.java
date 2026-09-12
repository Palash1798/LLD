package com.newsfeed.personalisednewsfeed.services;

import com.newsfeed.personalisednewsfeed.enums.PostType;
import com.newsfeed.personalisednewsfeed.models.Post;
import com.newsfeed.personalisednewsfeed.repositories.PostRepository;
import com.newsfeed.personalisednewsfeed.repositories.UserRepository;

/**
 * Handles post creation and engagement (likes).
 *
 * FEATURE 2 (MVP): create post
 * BONUS: like post (affects EngagementRankingStrategy score)
 */
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // -------------------------------------------------------------------------
    // FEATURE 2: Create post
    // -------------------------------------------------------------------------

    /**
     * Step 1 of write path (fan-out-on-read model):
     *   Store post globally — NO push to followers yet.
     *   Followers see it on their next getFeed() call.
     *
     * Fan-out-on-write extension: after save, push post id into each follower's feed_cache.
     */
    public Post createPost(String authorId, String content) {
        // Validate author exists
        userRepository.findById(authorId);

        Post post = new Post(authorId, content, PostType.TEXT);
        postRepository.save(post);
        System.out.println("[PostService] Created: " + post);
        return post;
    }

    // -------------------------------------------------------------------------
    // BONUS: Like post (engagement signal for ranking)
    // -------------------------------------------------------------------------

    /**
     * Increments likeCount — next getFeed() will re-rank with higher score.
     * Production: dedupe via likes(user_id, post_id) table.
     */
    public void likePost(String userId, String postId) {
        userRepository.findById(userId);
        Post post = postRepository.findById(postId);
        post.incrementLikeCount();
        System.out.println("[PostService] " + userId.substring(0, 8) + "... liked post -> likes="
                + post.getLikeCount());
    }
}
