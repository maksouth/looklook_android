package com.company.looklook.presentation.presenters.interfaces;

import android.arch.lifecycle.LifecycleObserver;

import com.company.looklook.domain.model.core.Post;

import java.util.List;

/**
 * Created by maksouth on 21.01.18.
 */

public interface SavedContract {
    interface View extends BaseContract.View {
        void showPosts(List<Post> posts);
        void updateItem(int position, int likes, int dislikes);
        void showNoPosts();
    }

    interface Presenter extends LifecycleObserver {
        void start();
        void onPostUpdateRequest(Post post);
    }
}
