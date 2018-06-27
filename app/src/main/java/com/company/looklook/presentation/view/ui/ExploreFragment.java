package com.company.looklook.presentation.view.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.company.looklook.R;
import com.company.looklook.data.impl.UserDataProviderImpl;
import com.company.looklook.data.impl.retrofit.RetrofitGetPostsUsecase;
import com.company.looklook.data.impl.retrofit.RetrofitRateUsecase;
import com.company.looklook.data.impl.room.RoomPollSimplePostsUsecase;
import com.company.looklook.data.impl.room.RoomSaveSimplePostUsecase;
import com.company.looklook.domain.datasources.PostDatabase;
import com.company.looklook.domain.datasources.UserDataProvider;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.infrastructure.SharedPreferencesWrapper;
import com.company.looklook.presentation.presenters.impl.ExplorePresenter;
import com.company.looklook.presentation.presenters.interfaces.ExploreContract;
import com.company.looklook.presentation.view.adapters.CardStackPostAdapter;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends BaseFragment implements ExploreContract.View {

    private static final String TAG = LookApplication.TAG_PREFIX +
            ExploreFragment.class.getSimpleName();

    private View emptyStateView;

    private CardStackView cardStackView;
    private ArrayAdapter<SimplePost> adapter;

    private ExploreContract.Presenter presenter;

    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyStateView = view.findViewById(R.id.empty_state);

        cardStackView = view.findViewById(R.id.card_stack_view);
        adapter = new CardStackPostAdapter(getContext());
        cardStackView.setAdapter(adapter);

        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {}

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                Log.d(TAG, "onCardSwiped");

                int position = cardStackView.getTopIndex();
                //because numeration in card stack view starts from 1, not 0
                position--;
                Log.d(TAG, "From stack: " + adapter.getItem(position));

                if (SwipeDirection.Left.equals(direction)) {
                    presenter.postDisliked(position);
                } else if (SwipeDirection.Right.equals(direction)) {
                    presenter.postLiked(position);
                }

            }

            @Override
            public void onCardReversed() {}

            @Override
            public void onCardMovedToOrigin() {}

            @Override
            public void onCardClicked(int index) {}
        });

        PostDatabase postDatabase = ((LookApplication)getActivity().getApplication()).getPostDatabase();
        SharedPreferencesWrapper spWrapper = new SharedPreferencesWrapper(getContext());
        UserDataProvider userDataProvider = new UserDataProviderImpl(spWrapper);
        presenter = new ExplorePresenter(this,
                new RoomPollSimplePostsUsecase(postDatabase.getSimplePostDataSource()),
                new RetrofitGetPostsUsecase(userDataProvider),
                new RoomSaveSimplePostUsecase(postDatabase.getSimplePostDataSource()),
                new RetrofitRateUsecase(userDataProvider));
        presenter.start();

    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    public void loadPosts(List<SimplePost> posts) {
        emptyStateView.setVisibility(View.GONE);
        cardStackView.setVisibility(View.VISIBLE);
        cardStackView.setPaginationReserved();
        adapter.addAll(posts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showNoPosts() {
        // TODO: 16.04.18 add pull to refresh view to load posts
        emptyStateView.setVisibility(View.VISIBLE);
        cardStackView.setVisibility(View.GONE);
    }

    // TODO: 03.03.18 check and refactor
    @Override
    protected ViewGroup getRootLayout() {
        return getActivity().findViewById(R.id.fragment_container);
    }
}
