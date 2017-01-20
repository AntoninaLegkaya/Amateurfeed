package com.dbbest.amateurfeed.app.storage.processor;

import com.dbbest.amateurfeed.model.AuthToken;

/**
 * Created by antonina on 20.01.17.
 */

public class DataProcessor {

    private AuthToken mAuthToken;

    public AuthToken authToken() {
        if (mAuthToken == null) {
            mAuthToken = new AuthToken();
        }
        return mAuthToken;
    }

}
