package com.company.looklook.domain.model;

public class RatePostResult {

    final private String postId;
    private int likes;
    private int dislikes;

    public RatePostResult(String postId, int likes, int dislikes) {
        this.postId = postId;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public RatePostResult(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
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
}
