package com.company.looklook.infrastructure.webservices;

import com.company.looklook.domain.model.web.RatePostRequestBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * service to rate post API
 */
public interface LookRatePostService {

    /*{
        "userToken": "user_token_888",
            "postId": "post_id_456",
            "like": true
    }*/

    @Headers(Parameters.CONTENT_TYPE_APPLICATION_JSON)
    @POST(Endpoints.RATE_POST)
    Call<ResponseBody> ratePost(@Body RatePostRequestBody body);

}
