package com.dbbest.amateurfeed;

import android.support.multidex.MultiDexApplication;

/**
 * Created by antonina on 19.01.17.
 */

public class App extends MultiDexApplication {
    private static App sInstance;

    public static App instance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
