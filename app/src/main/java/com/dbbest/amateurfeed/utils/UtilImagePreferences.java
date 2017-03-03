package com.dbbest.amateurfeed.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dbbest.amateurfeed.App;

public class UtilImagePreferences {

    private static final String PREF_IMAGE_PATH = ".img";
    public static final String KEY_IMAGE_PATH = "IMAGE_PATH";

    private static SharedPreferences preferences() {
        if (App.instance() != null) {
            return App.instance().getSharedPreferences(PREF_IMAGE_PATH, Context.MODE_PRIVATE);
        }
        return null;


    }

    public static final String getValue() {
        return preferences().getString(KEY_IMAGE_PATH, "");
    }

    public static final boolean putValue(String value) {
        value = value == null ? "" : value;
        SharedPreferences.Editor editor = preferences().edit();
        editor.putString(KEY_IMAGE_PATH, value);
        boolean result = editor.commit();
        if (!result) {
            return false;
        }
        return true;
    }

}
