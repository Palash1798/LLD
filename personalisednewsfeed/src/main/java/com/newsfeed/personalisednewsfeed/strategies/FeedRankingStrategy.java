package com.newsfeed.personalisednewsfeed.strategies;

import com.newsfeed.personalisednewsfeed.models.Post;
import com.newsfeed.personalisednewsfeed.models.User;

import java.util.List;

/**
 * STRATEGY PATTERN — pluggable feed ranking.
 *
 * Why Strategy here?
 *   Product can switch "Latest" vs "Top" vs A/B test weights without
 *   changing FeedService — Open/Closed Principle.
 *
 * Implementations:
 *   - ChronologicalRankingStrategy  → sort by createdAt DESC
 *   - EngagementRankingStrategy     → likes + recency decay (default)
 */
public interface FeedRankingStrategy {

    List<Post> rank(List<Post> posts, User viewer);
}
