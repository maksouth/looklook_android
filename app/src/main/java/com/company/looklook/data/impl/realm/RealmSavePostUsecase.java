package com.company.looklook.data.impl.realm;

import android.support.annotation.NonNull;

import com.company.looklook.domain.usecases.SavePostUsecase;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.realm.RealmPost;
import com.company.looklook.domain.model.mappers.PostModelMapper;

import io.reactivex.Observable;
import io.realm.Realm;

/**
 * Created by maksouth on 02.02.18.
 */

class RealmSavePostUsecase implements SavePostUsecase<Post> {

    //boolean indicating whether it is user saved posts or just cached remote posts
    private boolean isCachedRemote;

    public RealmSavePostUsecase(boolean isCachedRemote){
        this.isCachedRemote = isCachedRemote;
    }

    @Override
    public Observable<Post> store(@NonNull Post post) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmPost realmPost = PostModelMapper.toRealmPost(post, isCachedRemote);
        realm.copyToRealmOrUpdate(realmPost);
        realm.commitTransaction();

        realm.refresh();
        realm.close();

        return Observable.just(post);
    }
}
