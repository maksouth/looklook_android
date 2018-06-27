package com.company.looklook.presentation.presenters.interfaces;

import android.arch.lifecycle.LifecycleObserver;

import com.company.looklook.domain.model.core.SimplePost;

import java.util.List;

/**
 * Created by maksouth on 23.01.18.
 */

public interface ExploreContract {

    interface View extends BaseContract.View {
        void loadPosts(List<SimplePost> posts);
        void showNoPosts();
    }

    interface Presenter extends LifecycleObserver {
        void start();
        void postLiked(int position);
        void postDisliked(int position);
        void stop();
    }

}
