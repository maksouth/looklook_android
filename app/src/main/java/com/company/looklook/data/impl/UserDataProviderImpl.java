package com.company.looklook.data.impl;

import android.content.SharedPreferences;
import android.util.Log;

import com.company.looklook.domain.datasources.UserDataProvider;
import com.company.looklook.infrastructure.SharedPreferencesWrapper;
import com.company.looklook.presentation.view.ui.LookApplication;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.NoSuchElementException;

import io.reactivex.Single;
import io.reactivex.subjects.SingleSubject;

public class UserDataProviderImpl implements UserDataProvider{

    public static final String USER_TOKEN_KEY = "user_token_key";
    private static final String USER_TOKEN_DEFAULT_VALUE = "default_user_token";
    private static final String TAG = LookApplication.TAG_PREFIX +
            UserDataProviderImpl.class.getSimpleName();

    private SharedPreferences sharedPreferences;

    public UserDataProviderImpl(SharedPreferencesWrapper sharedPreferencesWrapper){
        this.sharedPreferences = sharedPreferencesWrapper.get();
    }

    @Override
    public Single<String> getToken() {
        SingleSubject<String> singleTokenSubject = SingleSubject.create();

        String prefUserToken = sharedPreferences.getString(USER_TOKEN_KEY, USER_TOKEN_DEFAULT_VALUE);

        if(FirebaseInstanceId.getInstance().getToken() != null) {
            Log.d(TAG, "Return token from Firebase service " + FirebaseInstanceId.getInstance().getToken());
            singleTokenSubject.onSuccess(FirebaseInstanceId.getInstance().getToken());
        } else if (!USER_TOKEN_DEFAULT_VALUE.equals(prefUserToken)) {
            Log.d(TAG, "Return token from preferences " + prefUserToken);
            singleTokenSubject.onSuccess(prefUserToken);
        } else {
            SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
                    if (USER_TOKEN_KEY.equals(key)) {

                        String tokenValue = sp.getString(key, USER_TOKEN_DEFAULT_VALUE);

                        if (!USER_TOKEN_DEFAULT_VALUE.equals(tokenValue)) {
                            Log.d(TAG, "Return token async from preferences " + tokenValue);
                            singleTokenSubject.onSuccess(tokenValue);
                            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

                        } else if (FirebaseInstanceId.getInstance().getToken() != null) {
                            Log.d(TAG, "Return token async from Firebase service " + FirebaseInstanceId.getInstance().getToken());
                            singleTokenSubject.onSuccess(FirebaseInstanceId.getInstance().getToken());
                        } else singleTokenSubject.onError(new NoSuchElementException());
                    }
                }
            };
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        }

        return singleTokenSubject;
    }
}
