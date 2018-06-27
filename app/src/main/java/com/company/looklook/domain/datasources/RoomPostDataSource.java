package com.company.looklook.domain.datasources;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.company.looklook.domain.model.room.RoomPost;

import java.util.List;

@Dao
public interface RoomPostDataSource {

    @Query("SELECT * FROM RoomPost")
    List<RoomPost> getPosts();

    @Query("SELECT * FROM RoomPost where id = :id LIMIT 1")
    RoomPost getPostById(String id);

    @Insert
    void insertPosts(List<RoomPost> posts);

    @Insert
    void insertPost(RoomPost post);

    @Update
    void updatePost(RoomPost post);

    @Delete
    void deletePost(RoomPost post);

}
