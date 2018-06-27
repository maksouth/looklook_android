package com.company.looklook.presentation.view.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.looklook.R;
import com.company.looklook.data.impl.room.RoomGetPostsUsecase;
import com.company.looklook.domain.datasources.PostDatabase;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.infrastructure.service.LookFirebaseMessagingService;
import com.company.looklook.presentation.presenters.impl.SavedPresenter;
import com.company.looklook.presentation.presenters.interfaces.SavedContract;
import com.company.looklook.presentation.view.adapters.SavedPostsAdapter;

import java.util.List;

import static com.company.looklook.infrastructure.service.LookFirebaseMessagingService.UpdatePostBroadcastScheme.DISLIKES_KEY;
import static com.company.looklook.infrastructure.service.LookFirebaseMessagingService.UpdatePostBroadcastScheme.ID_KEY;
import static com.company.looklook.infrastructure.service.LookFirebaseMessagingService.UpdatePostBroadcastScheme.LIKES_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
// TODO: 17.04.18 update post if notification with post rate result values received
public class SavedFragment extends BaseFragment implements SavedContract.View {

    private SavedContract.Presenter presenter;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private View emptyStateView;

    private BroadcastReceiver postUpdateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = intent.getStringExtra(ID_KEY);
            int likes = intent.getIntExtra(LIKES_KEY, Post.DEFAULT_NOT_AVAILABLE_INT);
            int dislikes = intent.getIntExtra(DISLIKES_KEY, Post.DEFAULT_NOT_AVAILABLE_INT);
            Post post = new Post(id);
            post.setLikes(likes);
            post.setDislikes(dislikes);

            presenter.onPostUpdateRequest(post);
        }
    };

    public SavedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyStateView = view.findViewById(R.id.empty_state);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        PostDatabase postDatabase = ((LookApplication)getActivity().getApplication()).getPostDatabase();
        presenter = new SavedPresenter(this, new RoomGetPostsUsecase(postDatabase.getPostDataSource()));
        presenter.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(postUpdateBroadcastReceiver,
                new IntentFilter(LookFirebaseMessagingService.UpdatePostBroadcastScheme.ACTION));
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(postUpdateBroadcastReceiver);
    }

    @Override
    public void showPosts(List<Post> posts) {
        emptyStateView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter = new SavedPostsAdapter(posts);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateItem(int position, int likes, int dislikes) {
        ((SavedPostsAdapter)mAdapter).updateItem(position, likes, dislikes);
    }

    @Override
    public void showNoPosts() {
        // TODO: 16.04.18 add pull to refresh view to load posts
        emptyStateView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    protected ViewGroup getRootLayout() {
        return getActivity().findViewById(R.id.fragment_container);
    }
}
