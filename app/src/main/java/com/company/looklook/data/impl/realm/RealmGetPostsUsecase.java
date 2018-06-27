package com.company.looklook.data.impl.realm;

import com.company.looklook.domain.model.realm.RealmPost;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.mappers.PostModelMapper;
import com.company.looklook.domain.usecases.GetPostsUsecase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

class RealmGetPostsUsecase implements GetPostsUsecase<Post> {

    private boolean isCachedRemote;
    private int lastPostIndexReturned;

    public RealmGetPostsUsecase(boolean isCachedRemote){
        this.isCachedRemote = isCachedRemote;
    }

    @Override
    public Observable<? extends List<Post>> getAll() {
        // defer - because fromCallable creates observable just once and then return same value
        // fromCallable - because defer should return Observable
        // have to think about
        return Observable.defer(() -> {
            Realm realm = Realm.getDefaultInstance();
            realm.refresh();
            RealmResults<RealmPost> realmPosts = getQuery(realm)
                    .findAll();
            List<Post> posts = new ArrayList<>();
            for (RealmPost realmPost : realmPosts) {
                Post post = PostModelMapper.toPost(realmPost);
                if(post != null && post.getId() != null && post.getId() != null) {
                    posts.add(PostModelMapper.toPost(realmPost));
                }
            }
            realm.close();
            return posts.size() > 0 ? Observable.just(posts)
                                    : Observable.empty();
        });
    }

    @Override
    public Observable<List<Post>> get(int limit) {
        return Observable.defer(() -> {
            Realm realm = Realm.getDefaultInstance();
            realm.refresh();
            RealmResults<RealmPost> realmPosts = getQuery(realm)
                    .findAll();
            List<Post> posts = new ArrayList<>();
            for (int i = lastPostIndexReturned; i < lastPostIndexReturned + limit && i < realmPosts.size(); i++ ) {
                Post post = PostModelMapper.toPost(realmPosts.get(i));
                if(post != null && post.getId() != null && post.getId() != null) {
                    posts.add(PostModelMapper.toPost(realmPosts.get(i)));
                }
            }
            realm.close();
            // TODO: 18.04.18 fix, if real size is less, than requested
            lastPostIndexReturned += limit;
            return posts.size() > 0 ? Observable.just(posts)
                    : Observable.empty();
        });
    }

    private RealmQuery<RealmPost> getQuery(Realm realm){
        RealmQuery<RealmPost> query = realm.where(RealmPost.class)
                .equalTo(PostScheme.IS_CACHED_REMOTE, isCachedRemote)
                .sort(PostScheme.TIMESTAMP, Sort.ASCENDING);

        if(isCachedRemote) {
            query.greaterThan(PostScheme.TIMESTAMP, System.currentTimeMillis());
        }

        return query;
    }

}
