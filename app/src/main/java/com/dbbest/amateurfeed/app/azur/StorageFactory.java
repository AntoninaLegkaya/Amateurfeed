package com.dbbest.amateurfeed.app.azur;

import android.app.Activity;
import android.content.Context;

import com.dbbest.amateurfeed.app.azur.exception.OperationException;
import com.dbbest.amateurfeed.app.azur.preferences.CloudPreferences;

import java.security.Provider;

public class StorageFactory {

    private static CloudPreferences prefs;


    public static Storage getStorageInstance(final Activity act)
            throws OperationException {

        Storage instance = null;
        Context ctx = act.getApplicationContext();

//        if (prefs == null) {
//            prefs = new CloudPreferences(ctx);
//        }

        instance = new AzureStorage(ctx);
        return instance;
    }
}
