package com.company.looklook.presentation.presenters.interfaces;

import android.support.annotation.StringRes;

/**
 * Created by maksouth on 21.01.18.
 */

public interface BaseContract {
    interface View {
        void showLoading(@StringRes int messageId);
        void hideLoading();
        void showError(@StringRes int messageResId);
    }
}
