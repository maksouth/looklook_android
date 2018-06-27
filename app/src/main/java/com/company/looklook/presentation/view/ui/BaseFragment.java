package com.company.looklook.presentation.view.ui;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.company.looklook.presentation.presenters.interfaces.BaseContract;

/**
 * Created by maksouth on 21.01.18.
 */

public abstract class BaseFragment extends Fragment implements BaseContract.View{

    private ProgressDialog progressDialog;

    @Override
    public void showLoading(@StringRes int messageResourceId) {
        if(progressDialog != null){
            progressDialog.dismiss();
        } else {
            progressDialog = new ProgressDialog(getContext());
        }

        progressDialog.setMessage(getString(messageResourceId));
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }

        progressDialog = null;
    }

    @Override
    public void showError(@StringRes int messageResourceId) {
        Snackbar.make(getRootLayout(), getString(messageResourceId), Snackbar.LENGTH_LONG).show();
    }

    protected abstract ViewGroup getRootLayout();

}
