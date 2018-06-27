package com.company.looklook.domain.model.core;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Simplified version of post, is used for API calls and as data model for external posts
 */
public class SimplePost implements Cloneable {

    @NonNull
    private String id;
    @NonNull
    private String imageUrl;
    private long timestamp;

    public SimplePost(@NonNull String id, @NonNull String imageUrl, long timestamp) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@NonNull String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Post clone() throws CloneNotSupportedException {
        return (Post) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePost post = (SimplePost) o;
        return Objects.equals(id, post.id) &&
                Objects.equals(imageUrl, post.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageUrl);
    }

    @Override
    public String toString() {
        return "SimplePost{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
