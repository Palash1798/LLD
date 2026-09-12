package com.newsfeed.personalisednewsfeed.strategies;

import com.newsfeed.personalisednewsfeed.models.Post;
import com.newsfeed.personalisednewsfeed.models.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Personalized ranking: engagement (likes) + recency decay.
 *
 * Score formula (say this in interview):
 *   score = likeCount * 10 + max(0, 100 - hoursOld)
 *
 * A post with 5 likes and posted 2 hours ago:
 *   score = 5*10 + (100-2) = 148
 *
 * A fresh post with 0 likes:
 *   score = 0 + 100 = 100  → still visible, but beaten by popular posts
 */
public class EngagementRankingStrategy implements FeedRankingStrategy {

    @Override
    public List<Post> rank(List<Post> posts, User viewer) {
        return posts.stream()
                .sorted(Comparator.comparingDouble(this::score).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Higher score = higher in feed.
     * viewer param reserved for future ML personalization (interests, click history).
     */
    private double score(Post post) {
        long ageHours = (System.currentTimeMillis() - post.getCreatedAt()) / 3_600_000L;
        double recencyBoost = Math.max(0, 100 - ageHours);
        return post.getLikeCount() * 10.0 + recencyBoost;
    }
}
