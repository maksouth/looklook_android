package com.company.looklook.domain.datasources;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.company.looklook.domain.model.room.RoomPost;
import com.company.looklook.domain.model.room.RoomSimplePost;

@Database(entities = {RoomPost.class, RoomSimplePost.class}, version = 1)
public abstract class PostDatabase extends RoomDatabase {

    private static PostDatabase database;

    public static PostDatabase getInstance(Context context) {
        if (database == null) {
            synchronized (PostDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(
                            context.getApplicationContext(),
                            PostDatabase.class,
                            "post_database.db"
                    ).build();
                }
            }
        }

        return database;
    }

    public abstract RoomPostDataSource getPostDataSource();

    public abstract RoomSimplePostDataSource getSimplePostDataSource();

}
