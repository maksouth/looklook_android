package com.company.looklook.presentation.presenters.impl;

import android.arch.lifecycle.LifecycleObserver;
import android.net.Uri;
import android.util.Log;

import com.company.looklook.R;
import com.company.looklook.domain.datasources.UserDataProvider;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.usecases.GetPostsUsecase;
import com.company.looklook.domain.usecases.RatePostUsecase;
import com.company.looklook.domain.usecases.SavePostUsecase;
import com.company.looklook.domain.usecases.UploadImageCommand;
import com.company.looklook.domain.usecases.UploadPostCommand;
import com.company.looklook.presentation.presenters.interfaces.UploadContract;
import com.company.looklook.presentation.view.ui.LookApplication;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UploadPresenter implements UploadContract.Presenter, LifecycleObserver{

    private static final String TAG = LookApplication.TAG_PREFIX +
            UploadPresenter.class.getSimpleName();
    private static final int RATE_POSTS_LIMIT = 3;

    private UploadContract.View view;
    private UploadPostCommand uploadPostCommand;
    private RatePostUsecase ratePostDataSource;
    private GetPostsUsecase<SimplePost> localGetRemotePostsDataSource;
    private GetPostsUsecase<SimplePost> remoteGetRemotePostsDataSource;
    private SavePostUsecase<SimplePost> localSaveRemotePostUsecase;
    private Disposable uploadDisposable;
    private List<SimplePost> ratePosts;
    private boolean areRatePostsShowed = false;

    public UploadPresenter(UploadContract.View view,
                           RatePostUsecase ratePostDataSource,
                           GetPostsUsecase<SimplePost> localGetRemotePostsDataSource,
                           GetPostsUsecase<SimplePost> remoteGetRemotePostsDataSource,
                           SavePostUsecase<Post> localSaveLocalPostUsecase,
                           SavePostUsecase<SimplePost> localSaveRemotePostUsecase,
                           SavePostUsecase<SimplePost> remoteSendLocalPostUsecase,
                           UserDataProvider userDataProvider) {
        this.view = view;
        this.ratePostDataSource = ratePostDataSource;
        this.localGetRemotePostsDataSource = localGetRemotePostsDataSource;
        this.remoteGetRemotePostsDataSource = remoteGetRemotePostsDataSource;
        this.localSaveRemotePostUsecase = localSaveRemotePostUsecase;

        uploadPostCommand = new UploadPostCommand(
                remoteSendLocalPostUsecase,
                localSaveLocalPostUsecase,
                new UploadImageCommand(),
                userDataProvider);
    }

    @Override
    public void start() {}

    @Override
    public void onImageLoad(Uri uri) {
        if (uri != null) {
            Post post = new Post(uri.toString(), new Date().getTime());
            uploadPostCommand.setPost(post);
            view.showImage(uri);
        } else {
            // TODO: 03.02.18 show error
        }
    }

    @Override
    public void uploadButtonClicked() {
        if (!areRatePostsShowed) {
            areRatePostsShowed = true;
            uploadDisposable = localGetRemotePostsDataSource.get(RATE_POSTS_LIMIT)
                    .switchIfEmpty(remoteGetRemotePostsDataSource.get(RATE_POSTS_LIMIT))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(s -> view.showLoading(R.string.loading_rate_posts))
                    .doOnTerminate(view::hideLoading)
                    .subscribe(posts -> {
                        view.setButtonText(R.string.upload);
                        if (posts.size() > 0) {
                            ratePosts = posts;
                            view.disableButton();
                            view.showRatePostsView();
                            view.loadPosts(posts);
                        } else {
                            continueUploading();
                            uploadPost();
                        }
                    }, err -> {
                        Log.d(TAG, err.toString(), err);
                        continueUploading();
                    });
        } else {
            uploadPost();
        }
    }

    @Override
    public void postLiked(int position) {
        doRate(position, true);
    }

    @Override
    public void postDisliked(int position) {
        doRate(position, false);
    }

    public void stop(){
        if (uploadDisposable != null)
            uploadDisposable.dispose();

        if (ratePosts != null && ratePosts.size() > 0) {
            for(int i = 0; i < ratePosts.size(); i++){
                localSaveRemotePostUsecase.store(ratePosts.get(i));
            }
        }
    }

    private void doRate(int position, boolean isLiked) {
        Log.d(TAG, "Post " + (isLiked ? "liked" : "disliked")
                + ", id: " + ratePosts.get(0).getId()
                + ", position " + position);


        if (ratePosts.size() > 0) {
            ratePostDataSource.ratePost(ratePosts.get(0), isLiked);
            ratePosts.remove(0);
        }

        if (ratePosts.size() == 0) {
            continueUploading();
        }
    }

    private void continueUploading() {
        view.hideRatePostsView();
        view.enableButton();
    }

    private void uploadPost() {
        uploadDisposable = uploadPostCommand.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(s -> {
                    Log.d(TAG, "Show loading in thread " + Thread.currentThread().getName() );
                    view.showLoading(R.string.loading_upload_post);})
                .doOnTerminate(() -> {
                    Log.d(TAG, "Hide loading");
                    view.hideLoading();
                })
                .subscribe(
                        () -> {
                            view.uploadComplete();
                            view.goToSavedScreen();
                        }, err -> {
                            Log.d(TAG, err.getMessage(), err);
                            view.showError(R.string.error_upload_failed);
                        });
    }

}
