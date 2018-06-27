package com.company.looklook.data.impl.realm;

import android.support.annotation.NonNull;

import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.realm.RealmPost;
import com.company.looklook.domain.model.mappers.PostModelMapper;
import com.company.looklook.domain.usecases.UpdatePostUsecase;

import io.reactivex.Observable;
import io.realm.Realm;

class RealmUpdatePostUsecase implements UpdatePostUsecase<Post, Post> {

    @Override
    public Observable<Post> update(@NonNull Post post) {
        Realm realm = Realm.getDefaultInstance();

        RealmPost realmPost = realm.where(RealmPost.class).equalTo(PostScheme.ID, post.getId()).findFirst();
        realmPost = updateRealmPost(realmPost, post);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmPost);
        realm.commitTransaction();

        realm.refresh();
        realm.close();

        return Observable.just(post);
    }

    private RealmPost updateRealmPost(RealmPost realmPost, Post post){

        if (realmPost == null) {
            realmPost = PostModelMapper.toRealmPost(post, false);
        } else {

            if (realmPost.timestamp == Post.DEFAULT_NOT_AVAILABLE_INT) {
                realmPost.timestamp = post.getTimestamp();
            }

            if (realmPost.imageUrl.equals(Post.DEFAULT_NOT_AVAILABLE_STRING)) {
                realmPost.imageUrl = post.getImageUrl();
            }

            if (post.getDislikes() > 0) {
                realmPost.dislikes = post.getDislikes();
            }

            if (post.getLikes() > 0) {
                realmPost.likes = post.getLikes();
            }

        }

        return realmPost;
    }

}
