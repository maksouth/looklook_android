package com.company.looklook.domain.usecases;

import io.reactivex.Completable;

public interface DeletePostByIdUsecase {
    Completable delete(String id);
}
