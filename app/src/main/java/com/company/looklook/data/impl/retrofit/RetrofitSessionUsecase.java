package com.company.looklook.data.impl.retrofit;

import android.util.Log;

import com.company.looklook.domain.datasources.UserDataProvider;
import com.company.looklook.domain.usecases.SessionInteractor;
import com.company.looklook.infrastructure.webservices.Endpoints;
import com.company.looklook.infrastructure.webservices.LookSessionService;
import com.company.looklook.infrastructure.webservices.Parameters;
import com.company.looklook.presentation.view.ui.LookApplication;

import java.util.HashMap;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSessionUsecase implements SessionInteractor {

    public static final String TAG = LookApplication.TAG_PREFIX +
            RetrofitSessionUsecase.class.getSimpleName();

    private LookSessionService service;
    private UserDataProvider userDataProvider;

    public RetrofitSessionUsecase(UserDataProvider userDataProvider) {

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

        service = retrofit.create(LookSessionService.class);
        this.userDataProvider = userDataProvider;
    }

    @Override
    public Completable openSession() {
        return userDataProvider.getToken().flatMapCompletable(token -> {
            Log.d(TAG, "open session with token " + token);
            HashMap<String, String> params = new HashMap<>();
            params.put(Parameters.USER_TOKEN, token);
            return Completable.fromObservable(service.openSession(params)
                                .subscribeOn(Schedulers.io()));
        });
    }

    @Override
    public Completable closeSession() {
        return userDataProvider.getToken().flatMapCompletable(token -> {
            Log.d(TAG, "close session with token " + token);
            HashMap<String, String> params = new HashMap<>();
            params.put(Parameters.USER_TOKEN, token);
            return Completable.fromObservable(service.closeSession(params)
                    .subscribeOn(Schedulers.io()));
        });
    }
}
