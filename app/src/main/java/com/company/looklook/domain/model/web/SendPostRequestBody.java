package com.company.looklook.domain.model.web;

import com.company.looklook.domain.model.core.SimplePost;

/**
 * this model is used for server POST request to publish post
 */
public class SendPostRequestBody {
    String userToken;
    SimplePost post;

    public SendPostRequestBody(String userToken, SimplePost post) {
        this.userToken = userToken;
        this.post = post;
    }

}
