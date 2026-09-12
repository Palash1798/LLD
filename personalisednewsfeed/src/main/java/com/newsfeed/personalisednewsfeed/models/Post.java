package com.newsfeed.personalisednewsfeed.models;

import com.newsfeed.personalisednewsfeed.enums.PostType;

/**
 * A single piece of content published by a user.
 *
 * Ranking inputs:
 *   - likeCount  → engagement signal (Strategy pattern)
 *   - createdAt  → recency signal (inherited from BaseModel)
 */
public class Post extends BaseModel {

    private final String authorId;
    private final String content;
    private final PostType type;
    private int likeCount;

    public Post(String authorId, String content, PostType type) {
        super();
        this.authorId = authorId;
        this.content = content;
        this.type = type;
        this.likeCount = 0;
    }

    public Post(String id, long createdAt, String authorId, String content, PostType type, int likeCount) {
        super(id, createdAt);
        this.authorId = authorId;
        this.content = content;
        this.type = type;
        this.likeCount = likeCount;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public PostType getType() {
        return type;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    @Override
    public String toString() {
        return "Post{" + getId().substring(0, 8) + "..., author=" + authorId.substring(0, 8)
                + "..., likes=" + likeCount + ", content='" + content + "'}";
    }
}
