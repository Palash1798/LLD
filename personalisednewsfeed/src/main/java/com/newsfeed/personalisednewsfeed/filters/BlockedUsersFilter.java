package com.newsfeed.personalisednewsfeed.filters;

import com.newsfeed.personalisednewsfeed.models.Post;
import com.newsfeed.personalisednewsfeed.models.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter step 1: remove posts from users the viewer has blocked.
 */
public class BlockedUsersFilter implements FeedFilter {

    @Override
    public List<Post> filter(List<Post> posts, User viewer) {
        return posts.stream()
                .filter(post -> !viewer.getBlockedUsers().contains(post.getAuthorId()))
                .collect(Collectors.toList());
    }
}
