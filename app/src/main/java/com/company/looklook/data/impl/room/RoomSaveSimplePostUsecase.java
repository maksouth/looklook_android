package com.company.looklook.data.impl.room;

import android.support.annotation.NonNull;

import com.company.looklook.domain.datasources.RoomSimplePostDataSource;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.model.mappers.RoomSimplePostMapper;
import com.company.looklook.domain.usecases.SavePostUsecase;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by maksouth on 18.04.18.
 */

public class RoomSaveSimplePostUsecase implements SavePostUsecase<SimplePost> {

    private RoomSimplePostDataSource roomPostDataSource;

    public RoomSaveSimplePostUsecase(RoomSimplePostDataSource roomPostDataSource) {
        this.roomPostDataSource = roomPostDataSource;
    }

    @Override
    public Observable<SimplePost> store(@NonNull SimplePost post) {
        return Observable.defer(() -> {
            roomPostDataSource.insert(RoomSimplePostMapper.to(post));
            return Observable.just(post);
        }).subscribeOn(Schedulers.io());
    }

}
