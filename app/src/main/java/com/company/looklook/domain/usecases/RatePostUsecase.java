package com.company.looklook.domain.usecases;

import com.company.looklook.domain.model.core.SimplePost;

import io.reactivex.Completable;

// TODO: 17.04.18 move somewhere if needed
public interface RatePostUsecase {

    Completable ratePost(SimplePost post, boolean isLike);

}
