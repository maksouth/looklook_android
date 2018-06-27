package com.company.looklook.data.impl.room;

import com.company.looklook.domain.datasources.RoomSimplePostDataSource;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.usecases.DeletePostsUsecase;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by maksouth on 19.04.18.
 */

public class RoomDeleteSimplePostsUsecase implements DeletePostsUsecase<SimplePost> {

    private RoomSimplePostDataSource simplePostDataSource;

    public RoomDeleteSimplePostsUsecase(RoomSimplePostDataSource simplePostDataSource) {
        this.simplePostDataSource = simplePostDataSource;
    }

    @Override
    public Completable execute() {
        return Completable.defer(() ->
                    Completable.fromRunnable(
                            () -> simplePostDataSource.delete(System.currentTimeMillis())))
                .subscribeOn(Schedulers.io());
    }
}
