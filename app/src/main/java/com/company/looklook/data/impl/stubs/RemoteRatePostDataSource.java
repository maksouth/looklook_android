package com.company.looklook.data.impl.stubs;

import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.usecases.RatePostUsecase;

import io.reactivex.Completable;

public class RemoteRatePostDataSource implements RatePostUsecase {

    @Override
    public Completable ratePost(SimplePost post, boolean isLike) {
        return Completable.complete();
    }
}
