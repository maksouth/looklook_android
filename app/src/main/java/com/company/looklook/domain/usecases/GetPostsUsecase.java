package com.company.looklook.domain.usecases;

import java.util.List;

import io.reactivex.Observable;

public interface GetPostsUsecase<T> {

    Observable<? extends List<T>> getAll();

    /**
     * return observable which will emit list
     * with {@code limit} number of posts
     * @param limit - max number of posts in list from observable
     * @return observable with list of max {@code limit} posts
     */
    Observable<List<T>> get(int limit);

}
