package com.company.looklook.domain.usecases;

import io.reactivex.Completable;

/**
 * Created by maksouth on 17.02.18.
 */

public interface SessionInteractor {

    Completable openSession();

    Completable closeSession();

}
