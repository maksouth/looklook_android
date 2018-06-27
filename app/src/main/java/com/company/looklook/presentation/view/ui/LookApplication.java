package com.company.looklook.presentation.view.ui;

import android.app.Application;

import com.company.looklook.data.impl.room.RoomDeleteSimplePostsUsecase;
import com.company.looklook.domain.datasources.PostDatabase;
import com.company.looklook.domain.model.realm.RealmPost;
import com.company.looklook.presentation.view.util.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by maksouth on 30.01.18.
 */

public class LookApplication extends Application {

    public static final String TAG_PREFIX = "[LOOK]";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        TypefaceUtil.overrideFont(getApplicationContext()); // font from assets: "assets/fonts/Roboto-Regular.ttf

        //addTestData();
        new RoomDeleteSimplePostsUsecase(getPostDatabase().getSimplePostDataSource())
                .execute();
    }

    public PostDatabase getPostDatabase() {
        return PostDatabase.getInstance(this);
    }

    private void addTestData(){
        List<RealmPost> posts = new ArrayList<>();
        // saved posts
        posts.add(new RealmPost(UUID.randomUUID().toString(), "https://i.pinimg.com/236x/06/8d/48/068d48e3f6ba1953181910c13163cfa7.jpg", System.currentTimeMillis(), false, 8, 2));
        posts.add(new RealmPost(UUID.randomUUID().toString(), "https://i.pinimg.com/736x/2e/94/97/2e9497cc2359a835e2612b972dd5575f--zara-united-kingdom-zara-united-states.jpg", System.currentTimeMillis(), false, 5, 5));
        posts.add(new RealmPost(UUID.randomUUID().toString(), "https://i.pinimg.com/564x/22/cc/70/22cc70485c52e4852e2b94c426f925eb.jpg", System.currentTimeMillis(), false, 2, 3));
        // cached remote posts
        posts.add(new RealmPost(UUID.randomUUID().toString(), "https://i.pinimg.com/236x/06/8d/48/068d48e3f6ba1953181910c13163cfa7.jpg", System.currentTimeMillis(), true));
        posts.add(new RealmPost(UUID.randomUUID().toString(), "https://i.pinimg.com/736x/2e/94/97/2e9497cc2359a835e2612b972dd5575f--zara-united-kingdom-zara-united-states.jpg", System.currentTimeMillis(), true));
        posts.add(new RealmPost(UUID.randomUUID().toString(), "https://i.pinimg.com/564x/22/cc/70/22cc70485c52e4852e2b94c426f925eb.jpg", System.currentTimeMillis(), true));

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(posts);
        realm.commitTransaction();
        realm.close();
    }
}
