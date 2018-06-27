package com.company.looklook.data.impl.room;

import android.support.annotation.NonNull;

import com.company.looklook.domain.datasources.RoomPostDataSource;
import com.company.looklook.domain.model.RatePostResult;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.room.RoomPost;
import com.company.looklook.domain.usecases.UpdatePostUsecase;

import io.reactivex.Observable;

public class RoomUpdatePostUsecase implements UpdatePostUsecase<RatePostResult, Post> {

    RoomPostDataSource postDataSource;

    public RoomUpdatePostUsecase(RoomPostDataSource postDataSource) {
        this.postDataSource = postDataSource;
    }

    @Override
    public Observable<Post> update(@NonNull RatePostResult ratePostResult) {
        return Observable.defer(() -> {
            RoomPost roomPost = postDataSource.getPostById(ratePostResult.getPostId());
            if(roomPost != null) {
                roomPost.setLikes(ratePostResult.getLikes());
                roomPost.setDislikes(ratePostResult.getDislikes());
                postDataSource.updatePost(roomPost);
                return Observable.empty();
            }
            return Observable.error(NullPointerException::new);
        });
    }
}
