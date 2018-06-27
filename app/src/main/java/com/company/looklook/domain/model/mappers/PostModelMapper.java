package com.company.looklook.domain.model.mappers;

import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.model.web.RatePostRequestBody;
import com.company.looklook.domain.model.RatePostResult;
import com.company.looklook.domain.model.realm.RealmPost;

public class PostModelMapper {

    public static Post toPost(RealmPost realmPost) {
        Post post = new Post(realmPost.id);
        post.setDislikes(realmPost.dislikes);
        post.setImageUrl(realmPost.imageUrl);
        post.setLikes(realmPost.likes);
        post.setTimestamp(realmPost.timestamp);

        return post;
    }

    public static RealmPost toRealmPost(Post post, boolean isCachedRemote){
        RealmPost realmPost = new RealmPost();
        realmPost.id = post.getId();
        realmPost.dislikes = post.getDislikes();
        realmPost.imageUrl = post.getImageUrl();
        realmPost.likes = post.getLikes();
        realmPost.timestamp = post.getTimestamp();
        realmPost.isCachedRemote = isCachedRemote;

        return realmPost;
    }

    public static SimplePost toPostSimple(Post post) {
        return new SimplePost(post.getId(), post.getImageUrl(), post.getTimestamp());
    }

    public static Post toPost(SimplePost simplePost) {
        return new Post(simplePost.getId(), simplePost.getImageUrl(), simplePost.getTimestamp());
    }

    public static Post toPost(RatePostResult ratePostResult) {
        Post post = new Post(ratePostResult.getPostId());
        post.setLikes(ratePostResult.getLikes());
        post.setDislikes(ratePostResult.getDislikes());
        return post;
    }

    public static RatePostRequestBody toRatePostRequestBody(String token, SimplePost post, boolean isLiked) {
        return new RatePostRequestBody(token, post.getId(), isLiked);
    }

}
