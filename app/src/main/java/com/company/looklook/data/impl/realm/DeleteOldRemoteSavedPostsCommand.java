package com.company.looklook.data.impl.realm;

import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.realm.RealmPost;

import io.realm.Realm;

/**
 * Created by maksouth on 10.02.18.
 */

class DeleteOldRemoteSavedPostsCommand {

    public void execute(){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(RealmPost.class)
                .equalTo(PostScheme.IS_CACHED_REMOTE, true)
                .lessThan(PostScheme.TIMESTAMP, System.currentTimeMillis())
                .findAll()
                .deleteAllFromRealm();
        realm.commitTransaction();
    }

}
