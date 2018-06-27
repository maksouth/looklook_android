package com.company.looklook.data.impl.stubs;

import android.support.annotation.NonNull;

import com.company.looklook.domain.usecases.SavePostUsecase;
import com.company.looklook.domain.model.core.Post;

import io.reactivex.Observable;

/**
 * Created by maksouth on 21.01.18.
 */

public class RemoteSendPostGateway implements SavePostUsecase<Post> {

    @Override
    public Observable<Post> store(@NonNull Post post) {
        return Observable.just(post);
    }
}
