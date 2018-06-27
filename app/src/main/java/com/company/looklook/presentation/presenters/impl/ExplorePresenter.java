package com.company.looklook.presentation.presenters.impl;

import android.util.Log;

import com.company.looklook.R;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.domain.usecases.GetPostsUsecase;
import com.company.looklook.domain.usecases.RatePostUsecase;
import com.company.looklook.domain.usecases.SavePostUsecase;
import com.company.looklook.presentation.presenters.interfaces.ExploreContract;
import com.company.looklook.presentation.view.ui.LookApplication;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

// TODO: 17.04.18 use empty state screen
public class ExplorePresenter implements ExploreContract.Presenter {

    private static final String TAG = LookApplication.TAG_PREFIX +
            ExplorePresenter.class.getSimpleName();
    //if less posts left we should ask for new posts
    private static final int REMAINING_POSTS_LIMIT = 5;
    private static final int LOAD_POSTS_PORTION = 10;

    private List<SimplePost> posts = new ArrayList<>();

    private ExploreContract.View view;
    private SavePostUsecase<SimplePost> localSavePostUsecase;
    private GetPostsUsecase<SimplePost> getLocalPostsDataSource;
    private GetPostsUsecase<SimplePost> getRemotePostsDataSource;
    private RatePostUsecase ratePostDataSource;
    private Disposable disposable;

    public ExplorePresenter(ExploreContract.View view,
                            GetPostsUsecase<SimplePost> getLocalPostsDataSource,
                            GetPostsUsecase<SimplePost> getRemotePostsDataSource,
                            SavePostUsecase<SimplePost> localSavePostUsecase,
                            RatePostUsecase ratePostDataSource) {
        this.view = view;
        this.getLocalPostsDataSource = getLocalPostsDataSource;
        this.getRemotePostsDataSource = getRemotePostsDataSource;
        this.ratePostDataSource = ratePostDataSource;
        this.localSavePostUsecase = localSavePostUsecase;
    }

    @Override
    public void start() {
        loadPosts(LOAD_POSTS_PORTION, v -> view.showLoading(R.string.loading_explore_posts));
    }

    @Override
    public void postLiked(int position) {
        doRate(position, true);
    }

    @Override
    public void postDisliked(int position) {
        doRate(position, false);
    }

    public void stop() {
        if (disposable != null) {
            disposable.dispose();
        }

        if(posts.size() > 0) {
            for(int i = 0; i < posts.size(); i++) {
                // TODO: 20.04.18 composit disposable
                localSavePostUsecase.store(posts.get(i)).subscribe();
            }
        }
    }

    private void doRate(int position, boolean isLiked) {
        if (!posts.isEmpty()) {
            Log.d(TAG, "From presenter(" + (isLiked ? "liked" : "disliked")
                    + ") [" + position + "] " + posts.get(0));
            ratePostDataSource.ratePost(posts.get(0), isLiked);
            posts.remove(0);
        }
        checkIfNeedToLoadNewPosts();
    }

    private void loadPosts(int postLimit, Consumer<? super Disposable> onSubscribeAction){
        disposable = getLocalPostsDataSource.get(postLimit)
                .switchIfEmpty(getRemotePostsDataSource.get(postLimit))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onSubscribeAction)
                .doOnTerminate(() -> {
                    if (posts.size() == 0) {
                        view.showNoPosts();
                    }
                    view.hideLoading();
                })
                .subscribe(posts -> {
                    ExplorePresenter.this.posts.addAll(posts);
                    view.loadPosts(posts);
                }, err ->  {
                    Log.d(TAG, err.toString(), err);
                    view.showError(R.string.error_loading_posts);
                });
    }

    private void checkIfNeedToLoadNewPosts() {
        //if (cardStackView.getTopIndex() == adapter.getCount() - 5) {
        if(posts.isEmpty()) {
            //view.showLoading(R.string.loading_explore_posts);
        }

        if(posts.size()  < REMAINING_POSTS_LIMIT) {
            /*
             do not show loading when requesting new posts can cause bad UX
            */
            loadPosts(LOAD_POSTS_PORTION, v -> {});
        }
    }

}
