package com.dbbest.amateurfeed;

import android.support.multidex.MultiDexApplication;

import com.dbbest.amateurfeed.app.net.retrofit.ApiFactory;

/**
 * Created by antonina on 19.01.17.
 */

public class App extends MultiDexApplication {
    private static App sInstance;
    private static ApiFactory sApiFactory;

    public static App instance() {
        return sInstance;
    }

    public static ApiFactory getFactory() {
        if (sApiFactory == null) {
            sApiFactory = new ApiFactory();
        }
        return sInstance.sApiFactory;


    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
