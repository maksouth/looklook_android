package com.company.looklook.data.impl.retrofit;

import android.util.Log;

import com.company.looklook.domain.datasources.UserDataProvider;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.usecases.GetPostsUsecase;
import com.company.looklook.infrastructure.webservices.Endpoints;
import com.company.looklook.infrastructure.webservices.LookPostService;
import com.company.looklook.presentation.view.ui.LookApplication;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by maksouth on 05.02.18.
 */
public class RetrofitGetPostsUsecase implements GetPostsUsecase<SimplePost> {

    private static final String TAG = LookApplication.TAG_PREFIX +
            RetrofitGetPostsUsecase.class.getSimpleName();
    private LookPostService service;
    private UserDataProvider userDataProvider;

    public RetrofitGetPostsUsecase(UserDataProvider userDataProvider) {
        this.userDataProvider = userDataProvider;

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
    }

    @Override
    public Observable<? extends List<SimplePost>> getAll() {
        return userDataProvider.getToken()
                .observeOn(Schedulers.io())
                .flatMapObservable(token -> service.getPosts(token));
    }

    @Override
    public Observable<List<SimplePost>> get(int limit) {
        Log.d(TAG, "start getting");
        return userDataProvider.getToken()
                .observeOn(Schedulers.io())
                .flatMapObservable(token -> service.getPosts(limit, token)
                                            .flatMap(Observable::fromIterable)
                                            .filter(post -> post != null)
                                            .toList()
                                            .toObservable()
                                            .doOnNext(list -> Log.d(TAG, "obtained posts " + list)));
    }

}
