package com.company.looklook.infrastructure.webservices;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LookSessionService {

    @Headers(Parameters.CONTENT_TYPE_APPLICATION_JSON)
    @POST(Endpoints.OPEN_SESSION)
    Observable<ResponseBody> openSession(@Body Map<String, String> params);

    @Headers(Parameters.CONTENT_TYPE_APPLICATION_JSON)
    @POST(Endpoints.CLOSE_SESSION)
    Observable<ResponseBody> closeSession(@Body Map<String, String> params);

}
