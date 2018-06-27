package com.company.looklook.data.impl.retrofit;

import android.support.annotation.NonNull;

import com.company.looklook.domain.datasources.UserDataProvider;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.model.web.SendPostRequestBody;
import com.company.looklook.domain.usecases.SavePostUsecase;
import com.company.looklook.infrastructure.webservices.Endpoints;
import com.company.looklook.infrastructure.webservices.LookPostService;
import com.company.looklook.presentation.view.ui.LookApplication;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSendPostGateway implements SavePostUsecase<SimplePost> {

    private static final String TAG = LookApplication.TAG_PREFIX +
            RetrofitSendPostGateway.class.getSimpleName();

    private LookPostService service;
    private UserDataProvider userDataProvider;

    public RetrofitSendPostGateway(UserDataProvider userDataProvider) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Endpoints.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(LookPostService.class);
        this.userDataProvider = userDataProvider;
    }

    @Override
    public Observable<SimplePost> store(@NonNull SimplePost post) {
        // TODO: 05.02.18 maybe something useful in response body
        PublishSubject<SimplePost> subject = PublishSubject.create();
        userDataProvider.getToken().subscribe(
                token -> service.sendPost(new SendPostRequestBody(token, post))
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                        subject.onNext(post);
                                        subject.onComplete();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                        subject.onError(t);
                                    }
                                }),
                subject::onError);

        return subject;
        //old rx code
//                .doOnError(err -> Log.d(TAG, "Error while sending post " + err.toString(), err))
//                .doOnNext(responseBody -> {
//                    Log.d(TAG, "Response START");
//                    try(BufferedReader bufferedReader = new BufferedReader(responseBody.charStream())) {
//                        String line;
//                        while ((line = bufferedReader.readLine()) != null) {
//                            Log.d(TAG, line);
//                        }
//                    } catch (Exception e) {
//                        Log.d(TAG, e.toString(), e);
//                    }
//                    Log.d(TAG, "Response END");
//                })
//                .map(responseBody ->  post);
    }

}
