package com.company.looklook.infrastructure.service;

import android.util.Log;

import com.company.looklook.data.impl.UserDataProviderImpl;
import com.company.looklook.infrastructure.SharedPreferencesWrapper;
import com.company.looklook.presentation.view.ui.LookApplication;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class LookFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = LookApplication.TAG_PREFIX +
            LookFirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.d(TAG, "Token refreshed " + FirebaseInstanceId.getInstance().getToken());
        SharedPreferencesWrapper wrapper = new SharedPreferencesWrapper(this);
        wrapper.get()
                .edit()
                .putString(UserDataProviderImpl.USER_TOKEN_KEY, FirebaseInstanceId.getInstance()
                                                                                    .getToken())
                .commit();
    }

}
