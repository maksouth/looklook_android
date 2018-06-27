package com.company.looklook.domain.model.mappers;

import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.room.RoomPost;

public class RoomPostMapper {

    public static RoomPost to(Post post) {
        return new RoomPost(post.getId(),
                post.getImageUrl(),
                post.getTimestamp(),
                post.getLikes(),
                post.getDislikes());
    }

    public static Post from(RoomPost roomPost) {
        return new Post(roomPost.getId(),
                roomPost.getImageUrl(),
                roomPost.getTimestamp(),
                roomPost.getLikes(),
                roomPost.getDislikes());
    }
}
