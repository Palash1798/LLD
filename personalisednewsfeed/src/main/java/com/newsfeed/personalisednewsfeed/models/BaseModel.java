package com.newsfeed.personalisednewsfeed.models;

import java.util.UUID;

/**
 * Common fields shared by domain entities.
 *
 * Step 0 in most LLD projects: every entity gets an id + createdAt timestamp.
 */
public abstract class BaseModel {

    private final String id;
    private final long createdAt;

    protected BaseModel() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = System.currentTimeMillis();
    }

    protected BaseModel(String id, long createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
