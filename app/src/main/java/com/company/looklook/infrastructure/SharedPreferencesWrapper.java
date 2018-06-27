package com.company.looklook.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesWrapper {

    private static final String SHARED_PREFERENCES_FILE_NAME = "look_shared_preferences";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesWrapper(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(
                SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public SharedPreferences get() {
        return sharedPreferences;
    }

}
