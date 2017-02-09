package com.dbbest.amateurfeed.model;

import android.common.util.TextUtils;
import android.content.Context;
import android.content.SharedPreferences;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.ui.util.exception.NotImplementedException;

/**
 * Created by antonina on 19.01.17.
 */

public class AuthToken implements ActiveRecord<AuthToken> {

    private static final String AUTH_FILE_NAME = ".auth";
    private static final String KEY_AUTH_TOKEN = "AUTH_TOKEN";
    private static final String BEARER = "Bearer ";

    public AuthToken() {

    }


    public String value() {
        return readToken();
    }

    public String bearer() {
        String token = readToken();
        if (TextUtils.isEmpty(token)) {
            return null;
        }
        return BEARER + token;
//        return token;
    }


    public void update(String token) {
        writeToken(token);
    }

    @Override
    public void purge() {
        update(null);
    }

    @Override
    public boolean isValid() {
        String token = readToken();
        return !TextUtils.isEmpty(token);
    }

    @Override
    public void subscribeChanges(OnRecordChangeListener<AuthToken> listener) {
        throw new NotImplementedException("subscribe changes");
    }

    @Override
    public void unsubscribeChanges() {
        throw new NotImplementedException("subscribe changes");
    }

    @Override
    public String toString() {
        String token = readToken();
        return "AccessToken{"
                + "mToken='" + token + '\''
                + '}';
    }


    private String readToken() {
        if (preferences() != null) {
            return preferences().getString(KEY_AUTH_TOKEN, null);
        }
        return null;
    }


    private void writeToken(String value) {
        preferences().edit().putString(KEY_AUTH_TOKEN, value).apply();
    }


    private SharedPreferences preferences() {
        if (App.instance() != null) {
            return App.instance().getSharedPreferences(AUTH_FILE_NAME, Context.MODE_PRIVATE);
        }
        return null;


    }
}
