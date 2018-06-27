package com.company.looklook.presentation.presenters.interfaces;

import android.arch.lifecycle.LifecycleObserver;
import android.net.Uri;
import android.support.annotation.StringRes;

import com.company.looklook.domain.model.core.SimplePost;

import java.util.List;

public interface UploadContract {

    interface View extends BaseContract.View {
        void showImage(Uri uri);
        void uploadComplete();
        void goToSavedScreen();

        void disableButton();
        void enableButton();
        void setButtonText(@StringRes int stringId);
        void loadPosts(List<SimplePost> posts);
        void showRatePostsView();
        void hideRatePostsView();
    }

    interface Presenter extends LifecycleObserver {
        void start();
        void stop();
        void onImageLoad(Uri uri);
        void uploadButtonClicked();
        void postLiked(int position);
        void postDisliked(int position);
    }

}
