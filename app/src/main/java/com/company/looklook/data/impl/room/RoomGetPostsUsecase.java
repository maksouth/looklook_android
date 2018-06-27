package com.company.looklook.data.impl.room;

import com.company.looklook.domain.datasources.RoomPostDataSource;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.mappers.RoomPostMapper;
import com.company.looklook.domain.model.room.RoomPost;
import com.company.looklook.domain.usecases.GetPostsUsecase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class RoomGetPostsUsecase implements GetPostsUsecase<Post> {

    RoomPostDataSource roomPostDataSource;

    public RoomGetPostsUsecase(RoomPostDataSource roomPostDataSource) {
        this.roomPostDataSource = roomPostDataSource;
    }

    @Override
    public Observable<List<Post>> getAll() {
        return Observable.defer(() -> {
            List<RoomPost> roomPosts = roomPostDataSource.getPosts();
            List<Post> posts = new ArrayList<>();

            for (RoomPost post : roomPosts) {
                posts.add(RoomPostMapper.from(post));
            }

            return Observable.just(posts);
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Post>> get(int limit) {
        // TODO: 18.04.18 poka pohui, peredelat' na paging, paging arch comps??
        return Observable.defer(() -> {
            List<RoomPost> roomPosts = roomPostDataSource.getPosts();
            List<Post> posts = new ArrayList<>();

            for (RoomPost post : roomPosts) {
                posts.add(RoomPostMapper.from(post));
            }

            return Observable.just(posts);
        }).subscribeOn(Schedulers.io());
    }
}
