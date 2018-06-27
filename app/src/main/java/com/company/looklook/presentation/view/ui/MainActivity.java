package com.company.looklook.presentation.view.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.company.looklook.R;
import com.company.looklook.data.impl.UserDataProviderImpl;
import com.company.looklook.domain.datasources.UserDataProvider;
import com.company.looklook.data.impl.retrofit.RetrofitSessionUsecase;
import com.company.looklook.infrastructure.SharedPreferencesWrapper;
import com.company.looklook.presentation.presenters.impl.MainPresenter;
import com.company.looklook.presentation.presenters.interfaces.MainContract;
import com.company.looklook.presentation.view.util.ImageChooserUtil;
import com.company.looklook.presentation.view.listeners.NavigationListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.company.looklook.presentation.view.ui.UploadFragment.EXTRA_IMAGE_URI;

public class MainActivity extends AppCompatActivity implements NavigationListener, MainContract.View {

    private static final int SELECT_PICTURE_INTENT = 1337;

    private static final String TAG = LookApplication.TAG_PREFIX +
            MainActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_PICK = 2559;
    private static final int REQUEST_CAMERA_PERMISSION = 337;

    private ImageChooserUtil imageChooserUtil;
    private FloatingActionButton uploadFab;
    private BottomNavigationView bottomMenu;
    private FragmentManager fragmentManager;
    private MainContract.Presenter presenter;

    private Map<String, Fragment> cachedFragments = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferencesWrapper spWrapper = new SharedPreferencesWrapper(this);
        UserDataProvider userDataProvider = new UserDataProviderImpl(spWrapper);
        presenter = new MainPresenter(this, new RetrofitSessionUsecase(userDataProvider));
        imageChooserUtil = new ImageChooserUtil(getApplicationContext());

        uploadFab = findViewById(R.id.fab);
        uploadFab.setOnClickListener(v -> presenter.onUploadClicked());

        bottomMenu = findViewById(R.id.bottom_navigation);
        bottomMenu.setOnNavigationItemSelectedListener(this::bottomMenuItemClicked);

        fragmentManager = getSupportFragmentManager();

        cachedFragments.put(UploadFragment.class.getSimpleName(), new UploadFragment());
        cachedFragments.put(SavedFragment.class.getSimpleName(), new SavedFragment());
        cachedFragments.put(ExploreFragment.class.getSimpleName(), new ExploreFragment());

        switchFragment(ExploreFragment.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    public void pickImage(){
        Intent pickFromGalleryIntent =
                new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickFromGalleryIntent, SELECT_PICTURE_INTENT);
    }

    @Override
    public void goToUploadFragment(Uri uri) {
        Fragment fragment = cachedFragments.get(UploadFragment.class.getSimpleName());
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_IMAGE_URI, uri);
        fragment.setArguments(bundle);
        switchFragment(UploadFragment.class);
        bottomMenu.setSelectedItemId(R.id.empty);
    }

    @Override
    public void goToSavedScreen() {
        switchFragment(SavedFragment.class);
        bottomMenu.setSelectedItemId(R.id.action_saved);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE_INTENT) {
                Uri selectedImageUri = data.getData();

                if (selectedImageUri != null) {
                    Log.d(TAG, "Image Uri " + selectedImageUri.getPath());
                    presenter.onImageLoad(selectedImageUri);
                } else {
                    Log.d(TAG, "Picked image is null");
                }
            }
        }
    }

    private boolean bottomMenuItemClicked(@NonNull MenuItem item){

        switch (item.getItemId()) {
            case R.id.action_explore:
                switchFragment(ExploreFragment.class);
                break;
            case R.id.action_saved:
                switchFragment(SavedFragment.class);
                break;
        }

        return true;
    }

    private void switchFragment(Class className){
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, cachedFragments.get(className.getSimpleName()))
                .commit();
    }
}
