package com.company.looklook.domain.datasources;

import io.reactivex.Single;

/**
 * stores, retrieve and provide user data
 */
public interface UserDataProvider {

    /**
     * @return FCM device unique token
     */
    Single<String> getToken();
}
