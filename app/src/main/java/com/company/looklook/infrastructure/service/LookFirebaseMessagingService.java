package com.company.looklook.infrastructure.service;

import android.content.Intent;
import android.util.Log;

import com.company.looklook.data.impl.room.RoomSaveSimplePostUsecase;
import com.company.looklook.data.impl.room.RoomUpdatePostUsecase;
import com.company.looklook.domain.datasources.PostDatabase;
import com.company.looklook.domain.model.RatePostResult;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.usecases.SavePostUsecase;
import com.company.looklook.domain.usecases.UpdatePostUsecase;
import com.company.looklook.presentation.view.ui.LookApplication;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static com.company.looklook.infrastructure.service.LookFirebaseMessagingService.NotificationScheme.DISLIKES;
import static com.company.looklook.infrastructure.service.LookFirebaseMessagingService.NotificationScheme.LIKES;
import static com.company.looklook.infrastructure.service.LookFirebaseMessagingService.NotificationScheme.NOTIFICATION_TYPE;

public class LookFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = LookApplication.TAG_PREFIX +
            LookFirebaseMessagingService.class.getSimpleName();

    public interface UpdatePostBroadcastScheme {
        String ACTION = "UPDATE_POST_ACTION";
        String ID_KEY = "id";
        String LIKES_KEY = "likes";
        String DISLIKES_KEY = "dislikes";
    }

    interface NotificationScheme {
        String NOTIFICATION_TYPE = "type";
        String NOTIFICATION_TYPE_NEW = "rate_post"; // posts from other people, which have to be estimated
        String NOTIFICATION_TYPE_RESULT = "rate_result"; // personal post with likes/dislikes info
        String ID = "postId";
        String TIMESTAMP = "timestamp";
        String IMAGE_URL = "imageUrl";
        String LIKES = "likes";
        String DISLIKES = "dislikes";
    }

    private PostDatabase postDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        postDatabase = ((LookApplication)getApplication()).getPostDatabase();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> postParams = remoteMessage.getData();
            String type = postParams.get(NOTIFICATION_TYPE);

            if(type != null) {
                try {
                    switch (type) {
                        case NotificationScheme.NOTIFICATION_TYPE_NEW:
                            SimplePost simplePost = parseNewPost(postParams);
                            processNewPost(simplePost);
                            break;
                        case NotificationScheme.NOTIFICATION_TYPE_RESULT:
                            RatePostResult ratePostResult = parseRateResults(postParams);
                            processRatePost(ratePostResult);
                            break;

                        default:
                            Log.d(TAG, "Cannot recognize message type: " + type);
                    }
                } catch (IllegalArgumentException e){
                    Log.d(TAG, "While parsing " + e.toString());
                }

            } else {
                Log.d(TAG, "Message type is null, skipping..");
            }

        }
    }

    private void processNewPost(SimplePost simplePost) {
        SavePostUsecase<SimplePost> localSavePostUsecase
                = new RoomSaveSimplePostUsecase(postDatabase.getSimplePostDataSource());
        localSavePostUsecase.store(simplePost).subscribe();
    }

    private void processRatePost(RatePostResult ratePostResult) {
        UpdatePostUsecase<RatePostResult, Post> localUpdatePostUsecase
                = new RoomUpdatePostUsecase(postDatabase.getPostDataSource());
        localUpdatePostUsecase.update(ratePostResult).subscribe();
        broadcastRatePostResults(ratePostResult);
    }

    private SimplePost parseNewPost(Map<String, String> params) throws IllegalArgumentException {

        String postId = params.get(NotificationScheme.ID);
        String imageUrl = params.get(NotificationScheme.IMAGE_URL);
        String timestampStr = params.get(NotificationScheme.TIMESTAMP);

        if (postId == null) {
            throw new IllegalArgumentException("Post id in push message is null");
        } else if (imageUrl == null) {
            throw new IllegalArgumentException("Image url for post " + postId + " is null");
        } else {
            long timestamp;

            try {
                timestamp = Long.valueOf(timestampStr);
            } catch (NumberFormatException e) {
                timestamp = System.currentTimeMillis();
            }

            SimplePost simplePost = new SimplePost(postId, imageUrl, timestamp);
            Log.d(TAG, "Parsed new post: " + simplePost);
            return simplePost;
        }
    }

    private RatePostResult parseRateResults(Map<String, String> params) throws IllegalArgumentException {

        String postId = params.get(NotificationScheme.ID);

        if (postId == null) {
            throw new IllegalArgumentException("Post id in push message is null");
        } else {
            String likesStr = params.get(LIKES);
            String dislikesStr = params.get(DISLIKES);
            int likes = Post.DEFAULT_INVALID_INT;
            int dislikes = Post.DEFAULT_INVALID_INT;

            try {
                likes = Integer.valueOf(likesStr);
            } catch (NumberFormatException e) {
                Log.d(TAG, e.toString(), e);
            }

            try {
                dislikes = Integer.valueOf(dislikesStr);
            } catch (NumberFormatException e) {
                Log.d(TAG, e.toString(), e);
            }

            RatePostResult ratePostResult = new RatePostResult(postId, likes, dislikes);
            return ratePostResult;
        }
    }

    private void broadcastRatePostResults(RatePostResult post) {
        Intent intent = new Intent();
        intent.setAction(UpdatePostBroadcastScheme.ACTION);
        intent.putExtra(UpdatePostBroadcastScheme.ID_KEY, post.getPostId());
        intent.putExtra(UpdatePostBroadcastScheme.LIKES_KEY, post.getLikes());
        intent.putExtra(UpdatePostBroadcastScheme.DISLIKES_KEY, post.getDislikes());
        sendBroadcast(intent);
    }
}
