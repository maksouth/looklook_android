package com.company.looklook.infrastructure.webservices;

import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.model.web.SendPostRequestBody;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LookPostService {

    @GET(Endpoints.GET_POSTS)
    Observable<List<SimplePost>> getPosts(@Query(Parameters.USER_TOKEN) String userToken);

    @GET(Endpoints.GET_POSTS)
    Observable<List<SimplePost>> getPosts(@Query(Parameters.SIZE) int limit, @Query(Parameters.USER_TOKEN) String userToken);

    @Headers(Parameters.CONTENT_TYPE_APPLICATION_JSON)
    @POST(Endpoints.SEND_POST)
    Call<ResponseBody> sendPost(@Body SendPostRequestBody body);

}
