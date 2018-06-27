package com.company.looklook.domain.model.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.company.looklook.domain.model.core.Post;

/**
 * Created by maksouth on 18.04.18.
 */
@Entity
public class RoomSimplePost {

    @PrimaryKey
    @NonNull
    private String id;
    private String imageUrl;
    private long timestamp;

    public RoomSimplePost(@NonNull String id, String imageUrl, long timestamp) {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
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
    public String toString() {
        return "SimplePost{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
