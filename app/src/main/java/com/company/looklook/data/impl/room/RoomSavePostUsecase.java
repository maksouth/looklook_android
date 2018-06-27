package com.company.looklook.data.impl.room;

import android.support.annotation.NonNull;
import android.util.Log;

import com.company.looklook.domain.datasources.RoomPostDataSource;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.mappers.RoomPostMapper;
import com.company.looklook.domain.usecases.SavePostUsecase;
import com.company.looklook.presentation.view.ui.LookApplication;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class RoomSavePostUsecase implements SavePostUsecase<Post> {

    private static final String TAG = LookApplication.TAG_PREFIX +
            RoomSavePostUsecase.class.getSimpleName();

    private RoomPostDataSource roomPostDataSource;

    public RoomSavePostUsecase(RoomPostDataSource roomPostDataSource) {
        this.roomPostDataSource = roomPostDataSource;
    }

    @Override
    public Observable<Post> store(@NonNull Post post) {
        return Observable.fromCallable(() -> {
            roomPostDataSource.insertPost(RoomPostMapper.to(post));
            Log.d(TAG, "end storing " + post.getId());
            return post;
        }).subscribeOn(Schedulers.io());
    }
}
