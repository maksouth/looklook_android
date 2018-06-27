package com.company.looklook.domain.model.core;

import java.util.UUID;

public class Post implements Cloneable {

    //as default values for edge cases for int values
    // value is invalid
    public static final int DEFAULT_INVALID_INT = -111;
    // valu cannot be obtained
    public static final int DEFAULT_NOT_AVAILABLE_INT = -222;
    public static final String DEFAULT_NOT_AVAILABLE_STRING = "not_available";

    private final String id;
    private String imageUrl;
    private long timestamp;
    private int likes;
    private int dislikes;

    public Post(){
        id = UUID.randomUUID().toString();
    }

    public Post(String imageUrl, long timestamp) {
        this();
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public Post(String id){
        this.id = id;
        likes = DEFAULT_NOT_AVAILABLE_INT;
        dislikes = DEFAULT_NOT_AVAILABLE_INT;
        timestamp = DEFAULT_NOT_AVAILABLE_INT;
        imageUrl = DEFAULT_NOT_AVAILABLE_STRING;
    }

    public Post(String id, String imageUrl, long timestamp) {
        this(id);
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public Post(String id, String imageUrl, long timestamp, int likes, int dislikes) {
        this(id, imageUrl, timestamp);
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public String getId(){ return id; }

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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public Post clone() throws CloneNotSupportedException {
        return (Post) super.clone();
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
