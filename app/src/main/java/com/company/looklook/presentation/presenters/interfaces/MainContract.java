package com.company.looklook.presentation.presenters.interfaces;

import android.net.Uri;

/**
 * Created by maksouth on 03.02.18.
 */

public interface MainContract {

    interface View {
        void pickImage();
        void goToUploadFragment(Uri uri);
    }

    interface Presenter {
        void start();
        void stop();
        void onUploadClicked();
        void onImageLoad(Uri uri);
    }
}
