package com.company.looklook.domain.usecases;

import android.util.Log;

import com.company.looklook.domain.datasources.UserDataProvider;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.model.mappers.PostModelMapper;
import com.company.looklook.presentation.view.ui.LookApplication;

import java.util.UUID;

import io.reactivex.Completable;

// TODO: 17.04.18 test carefully
public class UploadPostCommand {

    public static final String TAG = LookApplication.TAG_PREFIX +
            UploadPostCommand.class.getSimpleName();

    private Post post;
    private SavePostUsecase<SimplePost> remoteSendPostGateway;
    private SavePostUsecase<Post> localSavePostUsecase;
    private UploadImageCommand uploadImageCommand;
    private UserDataProvider userDataProvider;

    public UploadPostCommand(SavePostUsecase<SimplePost> remoteSendPostGateway,
                             SavePostUsecase<Post> localSavePostUsecase,
                             UploadImageCommand uploadImageCommand,
                             UserDataProvider userDataProvider){
        this.remoteSendPostGateway = remoteSendPostGateway;
        this.localSavePostUsecase = localSavePostUsecase;
        this.uploadImageCommand = uploadImageCommand;
        this.userDataProvider = userDataProvider;
    }

    public void setPost(Post post){
        this.post = post;
    }

    public Completable execute(){
        return Completable.fromObservable(
            userDataProvider.getToken()
                    .map(this::createFileName)
                    .map(s -> {
                        uploadImageCommand.setImageName(s);
                        return uploadImageCommand;
                    })
                    .flatMapObservable(command -> command.execute(post.getImageUrl()))
                    .map(imageRemotePath -> {
                        Log.d(TAG, "Download path: " + imageRemotePath);
                        Post postWithRemoteImagePath = post.clone();
                        postWithRemoteImagePath.setImageUrl(imageRemotePath);
                        return postWithRemoteImagePath;
                    })
                    .map(PostModelMapper::toPostSimple)
                    .map(remoteSendPostGateway::store)
                    .map(p -> post)
                    .flatMap(localSavePostUsecase::store));
    }

    private String createFileName(String userToken) {
        String uid = UUID.randomUUID().toString();
        return "post_image_" + userToken.substring(0, 16) + uid.substring(0, 16);
    }

}
