package com.newsfeed.personalisednewsfeed.repositories;

import com.newsfeed.personalisednewsfeed.exceptions.PostNotFoundException;
import com.newsfeed.personalisednewsfeed.models.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory post store with a secondary index by author.
 *
 * postsByAuthor speeds up fan-out-on-read:
 *   getFeed → for each followedId → findByAuthor(followedId)
 *
 * Production index: (author_id, created_at DESC)
 */
public class PostRepository {

    private final Map<String, Post> posts = new HashMap<>();
    private final Map<String, List<Post>> postsByAuthor = new HashMap<>();

    public void save(Post post) {
        posts.put(post.getId(), post);

        // Maintain secondary index for fast fan-out
        postsByAuthor
                .computeIfAbsent(post.getAuthorId(), ignored -> new ArrayList<>())
                .add(post);
    }

    public Post findById(String postId) {
        Post post = posts.get(postId);
        if (post == null) {
            throw new PostNotFoundException(postId);
        }
        return post;
    }

    /**
     * Returns all posts by a given author (newest appended last in MVP).
     * Production: query with ORDER BY created_at DESC LIMIT N.
     */
    public List<Post> findByAuthor(String authorId) {
        List<Post> authorPosts = postsByAuthor.get(authorId);
        if (authorPosts == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(authorPosts);
    }
}
