package com.newsfeed.personalisednewsfeed.models;

import java.util.Collections;
import java.util.List;

/**
 * Paginated feed response returned to the client.
 *
 * Step 4 of the feed pipeline (after collect → filter → rank):
 *   slice the ranked list and attach cursor metadata for infinite scroll.
 */
public class FeedPage {

    private final List<Post> posts;
    private final String nextCursor;
    private final boolean hasMore;

    public FeedPage(List<Post> posts, String nextCursor, boolean hasMore) {
        this.posts = Collections.unmodifiableList(posts);
        this.nextCursor = nextCursor;
        this.hasMore = hasMore;
    }

    public List<Post> getPosts() {
        return posts;
    }

    /** Offset cursor for MVP; production would use post-id or timestamp cursor. */
    public String getNextCursor() {
        return nextCursor;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    @Override
    public String toString() {
        return "FeedPage{posts=" + posts.size() + ", nextCursor=" + nextCursor + ", hasMore=" + hasMore + "}";
    }
}
