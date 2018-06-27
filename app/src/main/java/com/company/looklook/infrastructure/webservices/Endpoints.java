package com.company.looklook.infrastructure.webservices;

public interface Endpoints {
    String BASE_URL = "https://looklook-backend.appspot.com";
    //String BASE_URL = "172.16.0.3:8080";

    String GET_POSTS = "/posts/get";

    String SEND_POST = "/post/send";
    String RATE_POST = "/post/rate";

    String OPEN_SESSION = "/session/open";
    String CLOSE_SESSION = "/session/close";

}
