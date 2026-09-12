package com.newsfeed.personalisednewsfeed.exceptions;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(String postId) {
        super("Post not found: " + postId);
    }
}
