package com.newsfeed.personalisednewsfeed.services;

import com.newsfeed.personalisednewsfeed.filters.BlockedUsersFilter;
import com.newsfeed.personalisednewsfeed.filters.FeedFilter;
import com.newsfeed.personalisednewsfeed.filters.OwnPostsFilter;
import com.newsfeed.personalisednewsfeed.models.FeedPage;
import com.newsfeed.personalisednewsfeed.models.Post;
import com.newsfeed.personalisednewsfeed.models.User;
import com.newsfeed.personalisednewsfeed.repositories.PostRepository;
import com.newsfeed.personalisednewsfeed.repositories.UserRepository;
import com.newsfeed.personalisednewsfeed.strategies.EngagementRankingStrategy;
import com.newsfeed.personalisednewsfeed.strategies.FeedRankingStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * HEART OF THE LLD — orchestrates the feed pipeline.
 *
 * FEATURE 3 (MVP): get personalized feed
 *
 * Pipeline (memorize for interview):
 *   1. COLLECT  — fan-out-on-read: fetch posts from each followed user
 *   2. DEDUPE   — same post should not appear twice
 *   3. FILTER   — Chain of Responsibility (blocked, own posts)
 *   4. RANK     — Strategy pattern (engagement / chronological)
 *   5. PAGINATE — slice + cursor
 */
public class FeedService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FeedRankingStrategy rankingStrategy;
    private final List<FeedFilter> filters;

    public FeedService(UserRepository userRepository, PostRepository postRepository) {
        this(userRepository, postRepository, new EngagementRankingStrategy());
    }

    public FeedService(UserRepository userRepository,
                       PostRepository postRepository,
                       FeedRankingStrategy rankingStrategy) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.rankingStrategy = rankingStrategy;

        // Filter chain order matters — run blocked first, then own posts
        this.filters = List.of(
                new BlockedUsersFilter(),
                new OwnPostsFilter()
        );
    }

    // -------------------------------------------------------------------------
    // FEATURE 3: Get personalized feed
    // -------------------------------------------------------------------------

    public FeedPage getFeed(String userId, int limit, String cursor) {
        User viewer = userRepository.findById(userId);
        System.out.println("\n[FeedService] Building feed for " + viewer.getName()
                + " (following " + viewer.getFollowing().size() + " users)");

        // Step 1: COLLECT — fan-out on read
        List<Post> candidates = collectCandidatePosts(viewer);
        System.out.println("[FeedService] Step 1 COLLECT -> " + candidates.size() + " candidate posts");

        // Step 2: DEDUPE — safety net if graph has overlapping paths
        candidates = dedupePosts(candidates);
        System.out.println("[FeedService] Step 2 DEDUPE  -> " + candidates.size() + " unique posts");

        // Step 3: FILTER — Chain of Responsibility
        List<Post> filtered = applyFilters(candidates, viewer);
        System.out.println("[FeedService] Step 3 FILTER  -> " + filtered.size() + " posts after filters");

        // Step 4: RANK — Strategy pattern
        List<Post> ranked = rankingStrategy.rank(filtered, viewer);
        System.out.println("[FeedService] Step 4 RANK    -> ordered by " + rankingStrategy.getClass().getSimpleName());

        // Step 5: PAGINATE — offset cursor for MVP
        FeedPage page = paginate(ranked, limit, cursor);
        System.out.println("[FeedService] Step 5 PAGE   -> returning " + page.getPosts().size() + " posts");
        return page;
    }

    /**
     * Fan-out-on-read: for each user Alice follows, pull all their posts.
     *
     * Production optimizations (mention in interview):
     *   - Parallel fetch per followed user
     *   - Limit posts per author (top 50 recent)
     *   - Precomputed feed_cache for push model
     */
    private List<Post> collectCandidatePosts(User viewer) {
        List<Post> candidates = new ArrayList<>();
        for (String followedId : viewer.getFollowing()) {
            candidates.addAll(postRepository.findByAuthor(followedId));
        }
        return candidates;
    }

    private List<Post> dedupePosts(List<Post> posts) {
        Set<String> seen = new HashSet<>();
        List<Post> unique = new ArrayList<>();
        for (Post post : posts) {
            if (seen.add(post.getId())) {
                unique.add(post);
            }
        }
        return unique;
    }

    /**
     * Runs each FeedFilter in sequence — Chain of Responsibility.
     * Adding a new filter = new class + add to list; FeedService logic unchanged.
     */
    private List<Post> applyFilters(List<Post> posts, User viewer) {
        List<Post> result = posts;
        for (FeedFilter filter : filters) {
            result = filter.filter(result, viewer);
        }
        return result;
    }

    /**
     * Simple offset pagination for MVP.
     * cursor=null → start at 0; cursor="10" → start at index 10.
     */
    private FeedPage paginate(List<Post> ranked, int limit, String cursor) {
        int start = (cursor == null || cursor.isBlank()) ? 0 : Integer.parseInt(cursor);
        if (start >= ranked.size()) {
            return new FeedPage(List.of(), null, false);
        }
        int end = Math.min(start + limit, ranked.size());
        List<Post> page = ranked.subList(start, end);
        boolean hasMore = end < ranked.size();
        String nextCursor = hasMore ? String.valueOf(end) : null;
        return new FeedPage(page, nextCursor, hasMore);
    }
}
