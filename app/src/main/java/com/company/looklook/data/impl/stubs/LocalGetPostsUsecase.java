package com.company.looklook.data.impl.stubs;

import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.usecases.GetPostsUsecase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class LocalGetPostsUsecase implements GetPostsUsecase<Post> {

    @Override
    public Observable<List<Post>> getAll() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post("https://i.pinimg.com/236x/06/8d/48/068d48e3f6ba1953181910c13163cfa7.jpg", System.currentTimeMillis()));
        posts.add(new Post("https://i.pinimg.com/736x/2e/94/97/2e9497cc2359a835e2612b972dd5575f--zara-united-kingdom-zara-united-states.jpg", System.currentTimeMillis()));
        posts.add(new Post("https://i.pinimg.com/564x/22/cc/70/22cc70485c52e4852e2b94c426f925eb.jpg", System.currentTimeMillis()));
        return Observable.defer(() -> Observable.just(posts)).delay(3, TimeUnit.SECONDS);
    }

    @Override
    public Observable<List<Post>> get(int limit) {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post("https://i.pinimg.com/236x/06/8d/48/068d48e3f6ba1953181910c13163cfa7.jpg", System.currentTimeMillis()));
        posts.add(new Post("https://i.pinimg.com/736x/2e/94/97/2e9497cc2359a835e2612b972dd5575f--zara-united-kingdom-zara-united-states.jpg", System.currentTimeMillis()));
        posts.add(new Post("https://i.pinimg.com/564x/22/cc/70/22cc70485c52e4852e2b94c426f925eb.jpg", System.currentTimeMillis()));
        List<Post> shortPosts = posts.subList(0, posts.size() > limit ? limit : posts.size());

        return Observable.defer(() -> Observable.just(shortPosts)).delay(3, TimeUnit.SECONDS);
    }

}
