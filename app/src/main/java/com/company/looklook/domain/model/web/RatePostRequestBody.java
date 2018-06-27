package com.company.looklook.domain.model.web;

public class RatePostRequestBody {

    String userToken;
    String postId;
    boolean like;

    public RatePostRequestBody(String userToken, String postId, boolean like) {
        this.userToken = userToken;
        this.postId = postId;
        this.like = like;
    }

    @Override
    public String toString() {
        return "RatePostRequestBody{" +
                "userToken='" + userToken + '\'' +
                ", postId='" + postId + '\'' +
                ", like=" + like +
                '}';
    }

}
