package com.company.looklook.data.impl.room;

import com.company.looklook.domain.datasources.RoomSimplePostDataSource;
import com.company.looklook.domain.usecases.DeletePostByIdUsecase;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class RoomDeleteSimplePostByIdUsecase implements DeletePostByIdUsecase {

    private RoomSimplePostDataSource simplePostDataSource;

    public RoomDeleteSimplePostByIdUsecase(RoomSimplePostDataSource simplePostDataSource) {
        this.simplePostDataSource = simplePostDataSource;
    }

    @Override
    public Completable delete(String id) {
        return Completable.fromRunnable(() -> simplePostDataSource.delete(id))
                .subscribeOn(Schedulers.io());
    }
}
