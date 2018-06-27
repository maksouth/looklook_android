package com.company.looklook.presentation.presenters.impl;

import android.net.Uri;
import android.util.Log;

import com.company.looklook.domain.usecases.SessionInteractor;
import com.company.looklook.presentation.presenters.interfaces.MainContract;
import com.company.looklook.presentation.view.ui.LookApplication;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = LookApplication.TAG_PREFIX +
            MainPresenter.class.getSimpleName();
    private MainContract.View view;
    private SessionInteractor sessionManager;
    private CompositeDisposable compositeDisposable;

    public MainPresenter(MainContract.View view,
                         SessionInteractor sessionInteractor) {
        this.view = view;
        this.sessionManager = sessionInteractor;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void start() {
        Disposable disposable = sessionManager.openSession().subscribe(
                ()->Log.d(TAG, "Session opened"),
                err -> Log.d(TAG, "Error while opening session " + err.toString(), err)
        );
        compositeDisposable.add(disposable);
    }

    @Override
    public void stop() {
        sessionManager.closeSession().subscribe(
                ()->Log.d(TAG, "Session closed"),
                err -> Log.d(TAG, "Error while closing session " + err.toString(), err)
        );
        compositeDisposable.dispose();
    }

    @Override
    public void onUploadClicked() {
        view.pickImage();
    }

    @Override
    public void onImageLoad(Uri uri) {
        view.goToUploadFragment(uri);
    }

}
