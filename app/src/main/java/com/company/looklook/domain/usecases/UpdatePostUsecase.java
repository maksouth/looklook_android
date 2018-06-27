package com.company.looklook.domain.usecases;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

public interface UpdatePostUsecase<T, V> {

    /**
     * @param post - updating values, for example can be rate post model
     *             which has new values for likes and dislikes
     * @return Observable which emits updated value
     */
    Observable<V> update(@NonNull T post);

}
