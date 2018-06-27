package com.company.looklook.data.impl.room;

import android.util.Log;

import com.company.looklook.domain.datasources.RoomSimplePostDataSource;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.model.mappers.RoomSimplePostMapper;
import com.company.looklook.domain.model.room.RoomSimplePost;
import com.company.looklook.domain.usecases.GetPostsUsecase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * read posts from db and remove them from db - poll
 */
public class RoomPollSimplePostsUsecase implements GetPostsUsecase<SimplePost> {

    private static final String TAG = RoomPollSimplePostsUsecase.class.getSimpleName();

    RoomSimplePostDataSource roomPostDataSource;

    public RoomPollSimplePostsUsecase(RoomSimplePostDataSource roomPostDataSource) {
        this.roomPostDataSource = roomPostDataSource;
    }

    @Override
    public Observable<List<SimplePost>> getAll() {
        return Observable.defer(() -> {
            List<RoomSimplePost> roomPosts = roomPostDataSource.getMultiple();
            roomPostDataSource.deleteMultiple(roomPosts);

            List<SimplePost> posts = new ArrayList<>();

            for (RoomSimplePost post : roomPosts) {
                posts.add(RoomSimplePostMapper.from(post));
            }

            return Observable.just(posts);
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<SimplePost>> get(int limit) {
        // TODO: 18.04.18 poka pohui, peredelat' na paging, paging arch comps??
        return Observable.defer(() -> {
            Log.d(TAG, "start getting");
            List<RoomSimplePost> roomPosts = roomPostDataSource.getMultiple();
            roomPostDataSource.deleteMultiple(roomPosts);

            if (roomPosts.size() == 0) {
                Log.d(TAG, "stop getting, empty");
                return Observable.empty();
            } else {
                List<SimplePost> posts = new ArrayList<>();

                for (RoomSimplePost post : roomPosts) {
                    posts.add(RoomSimplePostMapper.from(post));
                }
                Log.d(TAG, "stop getting");
                return Observable.just(posts);
            }
        }).subscribeOn(Schedulers.io());
    }

}
