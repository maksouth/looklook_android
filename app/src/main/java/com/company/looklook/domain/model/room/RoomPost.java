package com.company.looklook.domain.model.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class RoomPost {
    @PrimaryKey
    @NonNull
    private String id;
    private String imageUrl;
    private long timestamp;
    private int likes;
    private int dislikes;

    public RoomPost(String id, String imageUrl, long timestamp, int likes, int dislikes) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomPost roomPost = (RoomPost) o;

        if (!id.equals(roomPost.id)) return false;
        return imageUrl.equals(roomPost.imageUrl);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + imageUrl.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RoomPost{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", timestamp=" + timestamp +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                '}';
    }
}
