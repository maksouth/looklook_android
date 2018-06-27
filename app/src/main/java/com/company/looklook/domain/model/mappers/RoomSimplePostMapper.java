package com.company.looklook.domain.model.mappers;

import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.model.room.RoomSimplePost;

public class RoomSimplePostMapper {

    public static RoomSimplePost to(SimplePost post) {
        return new RoomSimplePost(post.getId(),
                post.getImageUrl(),
                post.getTimestamp());
    }

    public static SimplePost from(RoomSimplePost post) {
        return new SimplePost(post.getId(),
                post.getImageUrl(),
                post.getTimestamp());
    }

}
