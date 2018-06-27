package com.company.looklook.presentation.presenters.impl;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.company.looklook.R;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.usecases.GetPostsUsecase;
import com.company.looklook.presentation.presenters.interfaces.SavedContract;
import com.company.looklook.presentation.view.ui.LookApplication;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by maksouth on 21.01.18.
 */

public class SavedPresenter implements SavedContract.Presenter {

    private static final String TAG = LookApplication.TAG_PREFIX +
            SavedPresenter.class.getSimpleName();
    private SavedContract.View view;
    private GetPostsUsecase<Post> getPostsUsecase;
    private Disposable loadImagesDisposable;
    private List<Post> posts;

    public SavedPresenter(SavedContract.View view, GetPostsUsecase<Post> getPostsUsecase) {
        this.view = view;
        this.getPostsUsecase = getPostsUsecase;
    }

    @Override
    public void start() {
        loadImagesDisposable = getPostsUsecase.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(v -> view.showLoading(R.string.loading_saved_posts))
                .doOnTerminate(() -> {
                    if (posts.size() == 0) {
                        Log.d(TAG,  "Show no posts");
                        view.showNoPosts();
                    }
                    view.hideLoading();
                })
                .subscribe(posts -> {
                            Collections.reverse(posts);
                            this.posts = posts;
                            view.showPosts(posts);
                        },
                        err -> {
                            Log.d(TAG, err.getMessage(), err);
                            view.showError(R.string.error_load_images);
                });
    }

    @Override
    public void onPostUpdateRequest(Post post) {
        int position = -2;
        for(int i = 0; i < posts.size(); i++) {
            if(posts.get(i).getId().equals(post.getId())){
                position = i;
                break;
            }
        }

        if(position != -2) {
            view.updateItem(position, post.getLikes(), post.getDislikes());
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(){
        if (loadImagesDisposable != null)
            loadImagesDisposable.dispose();
    }

}
