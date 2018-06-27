package com.company.looklook.domain.usecases;

import android.support.annotation.NonNull;

import com.company.looklook.domain.model.core.Post;

import io.reactivex.Observable;

public interface SavePostUsecase<T> {

    Observable<T> store(@NonNull T post);

}
