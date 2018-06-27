package com.company.looklook.data.impl.retrofit;

import com.company.looklook.domain.datasources.UserDataProvider;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.model.mappers.PostModelMapper;
import com.company.looklook.domain.usecases.RatePostUsecase;
import com.company.looklook.infrastructure.webservices.Endpoints;
import com.company.looklook.infrastructure.webservices.LookRatePostService;

import io.reactivex.Completable;
import io.reactivex.subjects.CompletableSubject;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

// TODO: 04.03.18 DAGGER 2
public class RetrofitRateUsecase implements RatePostUsecase {

    private LookRatePostService service;
    private UserDataProvider userDataProvider;

    public RetrofitRateUsecase(UserDataProvider userDataProvider) {
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

        service = retrofit.create(LookRatePostService.class);
        this.userDataProvider = userDataProvider;
    }

    @Override
    public Completable ratePost(SimplePost post, boolean isLike) {
        return doRate(isLike, post);
    }

    private Completable doRate(boolean isLiked, SimplePost post) {
        CompletableSubject subject = CompletableSubject.create();

        userDataProvider.getToken().subscribe(
                userToken -> service.ratePost(PostModelMapper.toRatePostRequestBody(userToken, post, isLiked))
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            subject.onComplete();
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            subject.onError(t);
                                        }
                                    }),
                subject::onError
        );

        return subject;
    }
}
