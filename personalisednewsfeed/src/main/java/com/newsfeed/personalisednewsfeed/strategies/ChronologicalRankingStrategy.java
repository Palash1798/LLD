package com.newsfeed.personalisednewsfeed.strategies;

import com.newsfeed.personalisednewsfeed.models.Post;
import com.newsfeed.personalisednewsfeed.models.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * "Latest first" ranking — simplest Strategy implementation.
 *
 * Use when interviewer asks for a toggle between chronological and personalized.
 */
public class ChronologicalRankingStrategy implements FeedRankingStrategy {

    @Override
    public List<Post> rank(List<Post> posts, User viewer) {
        return posts.stream()
                .sorted(Comparator.comparingLong(Post::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
}
