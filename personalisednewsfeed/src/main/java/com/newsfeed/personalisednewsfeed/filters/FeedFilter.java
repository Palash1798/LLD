package com.newsfeed.personalisednewsfeed.filters;

import com.newsfeed.personalisednewsfeed.models.Post;
import com.newsfeed.personalisednewsfeed.models.User;

import java.util.List;

/**
 * CHAIN OF RESPONSIBILITY — one stage in the feed filter pipeline.
 *
 * Filters REMOVE posts that should not appear in the viewer's feed.
 * Ranking (Strategy) ORDERS what remains — different concern, different pattern.
 *
 * FeedService runs all filters in order before calling FeedRankingStrategy.
 */
public interface FeedFilter {

    List<Post> filter(List<Post> posts, User viewer);
}
