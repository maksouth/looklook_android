package com.company.looklook.domain.usecases;

import io.reactivex.Completable;

/**
 * remove old posts
 */
public interface DeletePostsUsecase<T> {
    Completable execute();
}
