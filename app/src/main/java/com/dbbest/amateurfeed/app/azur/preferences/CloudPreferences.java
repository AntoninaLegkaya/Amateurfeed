package com.dbbest.amateurfeed.app.azur.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.dbbest.amateurfeed.App;

public class CloudPreferences {

    /**
     * This application's preferences label
     */
    private static final String PREFS_NAME = "BlobberPrefs";

    /**
     * This application's preferences
     */
    private static SharedPreferences settings;
    private static SharedPreferences.Editor editor;

    /**
     * Cloud info fields
     */
    private static final String ACCT_NAME_KEY = "acctName";
    private static final String ACCESS_KEY = "acctKey";
    private static final String CONTAINER_KEY = "container";
    private static final String STORAGE_URL = "url";

    /**
     * Control access : if all fields above are filled
     */
    private static final String OK_KEY = "OK_KEY";


    private String readNameKey() {
        if (preferences() != null) {
            return preferences().getString(ACCT_NAME_KEY, null);
        }
        return null;
    }


    public void writeNameKey(String value) {
        preferences().edit().putString(ACCT_NAME_KEY, value).apply();
    }


    public String readAccessKey() {
        if (preferences() != null) {
            return preferences().getString(ACCESS_KEY, null);
        }
        return null;
    }


    public void writeAccessKey(String value) {
        preferences().edit().putString(ACCESS_KEY, value).apply();
    }


    public String readContainerKey() {
        if (preferences() != null) {
            return preferences().getString(CONTAINER_KEY, null);
        }
        return null;
    }


    public void writeContainerName(String value) {
        preferences().edit().putString(CONTAINER_KEY, value).apply();
    }

    private String readStorageUrl() {
        if (preferences() != null) {
            return preferences().getString(STORAGE_URL, null);
        }
        return null;
    }


    public void writeStorageUrl(String value) {
        preferences().edit().putString(STORAGE_URL, value).apply();
    }

    private SharedPreferences preferences() {
        if (settings == null) {
            settings = App.instance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();
            return settings;
        }
        return settings;


    }


    public CloudPreferences() {
//        if (settings == null) {
//            settings = ctx.getSharedPreferences(PREFS_NAME,
//                    Context.MODE_PRIVATE);
//            editor = settings.edit();
//        }
    }

    /***/
    public String getAccountName() {
        return preferences().getString(ACCT_NAME_KEY, "");
    }

    /***/
    public String getAccessKey() {
        return preferences().getString(ACCESS_KEY, "");
    }

    /***/
    public String getContainer() {
        return preferences().getString(CONTAINER_KEY, "");
    }

    /***/
    public String getStorageUrl() {
        return preferences().getString(STORAGE_URL, "");
    }


    public void setCredentials(String acctName, String acctKey, String container, String url) {

        preferences();
        editor.putString(ACCT_NAME_KEY, acctName);
        editor.putString(ACCESS_KEY, acctKey);
        editor.putString(CONTAINER_KEY, container);
        editor.putString(STORAGE_URL, url);

        // not allowed functionality unless all fields are filled
        boolean notFilled = isEmpty(acctName) ||
                isEmpty(acctKey) ||
                isEmpty(container);

        editor.putBoolean(OK_KEY, notFilled ? false : true);
        // commit changes
        editor.commit();
    }

    /***/
    private static boolean isEmpty(String input) {
        return (input == null || input.trim().length() == 0);
    }


    @Override
    public String toString() {

        return "Cloud preferences: " +
                "\nAccount Name: " + getAccountName() +
                "\nAccesskey: " + getAccessKey() +
                "\nContainer: " + getContainer() +
                "\nUrl: " + getStorageUrl();
    }
}