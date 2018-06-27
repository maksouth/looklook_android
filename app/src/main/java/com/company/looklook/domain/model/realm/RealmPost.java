package com.company.looklook.domain.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by maksouth on 31.01.18.
 */

public class RealmPost extends RealmObject {
    @PrimaryKey
    public String id;
    public String imageUrl;
    public long timestamp;
    public int likes;
    public int dislikes;
    //true - not user saved posts, just stored remote post
    public boolean isCachedRemote;

    public RealmPost(){}

    //only for development
    // TODO: 03.02.18 remove for production
    public RealmPost(String id, String imageUrl, long timestamp, boolean isCachedRemote){
        this.id = id;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.isCachedRemote = isCachedRemote;
    }

    public RealmPost(String id, String imageUrl, long timestamp, boolean isCachedRemote,
                     int likes, int dislikes){
        this.id = id;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.isCachedRemote = isCachedRemote;
        this.likes = likes;
        this.dislikes = dislikes;
    }

}
