package com.company.looklook.domain.datasources;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.company.looklook.domain.model.room.RoomSimplePost;

import java.util.List;

@Dao
public interface RoomSimplePostDataSource {

    @Query("SELECT * FROM RoomSimplePost")
    List<RoomSimplePost> getMultiple();

    @Insert
    void insert(List<RoomSimplePost> posts);

    @Insert
    void insert(RoomSimplePost post);

    @Update
    void update(RoomSimplePost post);

    @Delete
    void delete(RoomSimplePost post);

    @Delete
    void deleteMultiple(List<RoomSimplePost> posts);

    @Query("DELETE FROM RoomSimplePost where timestamp < :timestamp")
    void delete(long timestamp);

    @Query("DELETE FROM RoomSimplePost where id = :id")
    void delete(String id);

}
