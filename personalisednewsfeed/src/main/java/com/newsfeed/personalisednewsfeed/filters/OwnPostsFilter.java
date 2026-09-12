package com.newsfeed.personalisednewsfeed.filters;

import com.newsfeed.personalisednewsfeed.models.Post;
import com.newsfeed.personalisednewsfeed.models.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter step 2: remove the viewer's own posts from their feed.
 *
 * (Some products show own posts — clarify with interviewer; default here is hide.)
 */
public class OwnPostsFilter implements FeedFilter {

    @Override
    public List<Post> filter(List<Post> posts, User viewer) {
        return posts.stream()
                .filter(post -> !post.getAuthorId().equals(viewer.getId()))
                .collect(Collectors.toList());
    }
}
