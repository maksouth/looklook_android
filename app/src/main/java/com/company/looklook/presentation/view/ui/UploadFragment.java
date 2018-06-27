package com.company.looklook.presentation.view.ui;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.company.looklook.R;
import com.company.looklook.data.impl.UserDataProviderImpl;
import com.company.looklook.data.impl.retrofit.RetrofitGetPostsUsecase;
import com.company.looklook.data.impl.retrofit.RetrofitRateUsecase;
import com.company.looklook.data.impl.retrofit.RetrofitSendPostGateway;
import com.company.looklook.data.impl.room.RoomPollSimplePostsUsecase;
import com.company.looklook.data.impl.room.RoomSavePostUsecase;
import com.company.looklook.data.impl.room.RoomSaveSimplePostUsecase;
import com.company.looklook.domain.datasources.PostDatabase;
import com.company.looklook.domain.datasources.UserDataProvider;
import com.company.looklook.domain.model.core.SimplePost;
import com.company.looklook.infrastructure.SharedPreferencesWrapper;
import com.company.looklook.presentation.presenters.impl.UploadPresenter;
import com.company.looklook.presentation.presenters.interfaces.UploadContract;
import com.company.looklook.presentation.view.adapters.CardStackPostAdapter;
import com.company.looklook.presentation.view.listeners.NavigationListener;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends BaseFragment implements UploadContract.View {

    public static final String EXTRA_IMAGE_URI = "IMAGE_URI";
    private static final String TAG = LookApplication.TAG_PREFIX +
            UploadFragment.class.getSimpleName();

    private UploadContract.Presenter presenter;
    @Nullable
    private NavigationListener navigationListener;
    private ImageView imageView;

    private CardStackView cardStackView;
    private ConstraintLayout rateCardStackLayout;
    private Button uploadButton;

    private ArrayAdapter<SimplePost> adapter;
    private Uri uri;

    public UploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            try {
                navigationListener = (NavigationListener) getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException(getActivity().toString()
                        + " must implement NavigationListener");
            }
        }

        rateCardStackLayout = view.findViewById(R.id.rate_card_stack_fragment);
        hideRatePostsView();
        imageView = view.findViewById(R.id.image);
        uploadButton = view.findViewById(R.id.upload_button);
        cardStackView = view.findViewById(R.id.card_stack_view);

        uploadButton.setOnClickListener( v -> presenter.uploadButtonClicked());

        adapter = new CardStackPostAdapter(getContext());
        cardStackView.setAdapter(adapter);
        cardStackView.setPaginationReserved();
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                //numeration in card stack view starts from 1, not 0
                if (SwipeDirection.Left.equals(direction)) {
                    presenter.postDisliked(cardStackView.getTopIndex()-1);
                } else if (SwipeDirection.Right.equals(direction)) {
                    presenter.postLiked(cardStackView.getTopIndex()-1);
                }

            }

            @Override
            public void onCardReversed() {}

            @Override
            public void onCardMovedToOrigin() {}

            @Override
            public void onCardClicked(int index) {}
        });

        SharedPreferencesWrapper spWrapper = new SharedPreferencesWrapper(getContext());
        UserDataProvider userDataProvider = new UserDataProviderImpl(spWrapper);
        PostDatabase postDatabase = ((LookApplication)getActivity().getApplication()).getPostDatabase();

        presenter = new UploadPresenter(this,
                new RetrofitRateUsecase(userDataProvider),
                new RoomPollSimplePostsUsecase(postDatabase.getSimplePostDataSource()),
                new RetrofitGetPostsUsecase(userDataProvider),
                new RoomSavePostUsecase(postDatabase.getPostDataSource()),
                new RoomSaveSimplePostUsecase(postDatabase.getSimplePostDataSource()),
                new RetrofitSendPostGateway(userDataProvider),
                userDataProvider);
        getLifecycle().addObserver(presenter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            uri = getArguments().getParcelable(EXTRA_IMAGE_URI);
            presenter.onImageLoad(uri);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    public void showImage(Uri uri) {
        Log.d(TAG, "Upload image path: " + uri.toString());
        Picasso.with(getContext())
                .load(uri)
                .into(imageView);
    }

    @Override
    protected ViewGroup getRootLayout() {
        return getActivity().findViewById(R.id.fragment_container);
    }

    @Override
    public void uploadComplete() {
        Snackbar.make(getRootLayout(),
                R.string.upload_completed, Snackbar.LENGTH_LONG)
            .show();
    }

    @Override
    public void goToSavedScreen() {
        if (navigationListener != null) {
            navigationListener.goToSavedScreen();
        }
    }

    @Override
    public void disableButton() {
        uploadButton.setEnabled(false);
    }

    @Override
    public void enableButton() {
        uploadButton.setEnabled(true);
    }

    @Override
    public void setButtonText(@StringRes int stringId) {
        uploadButton.setText(getString(stringId));
    }

    @Override
    public void loadPosts(List<SimplePost> posts) {
        adapter.addAll(posts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showRatePostsView() {
        rateCardStackLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRatePostsView() {
        rateCardStackLayout.setVisibility(View.GONE);
    }
}
