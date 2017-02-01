package com.dbbest.amateurfeed;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.dbbest.amateurfeed.app.net.retrofit.ApiFactory;
import com.dbbest.amateurfeed.utils.FactoryImpl;
import com.dbbest.amateurfeed.utils.Utils;

/**
 * Created by antonina on 19.01.17.
 */

public class App extends MultiDexApplication {
    private static App sInstance;
    private static ApiFactory sApiFactory;
    private static FactoryImpl mFactory;

    public static App instance() {
        return sInstance;
    }

    public static ApiFactory getApiFactory() {
        if (sApiFactory == null) {
            sApiFactory = new ApiFactory();
        }
        return sInstance.sApiFactory;


    }
    public static FactoryImpl getFactory() {
        if (mFactory == null) {
            mFactory = new FactoryImpl();
        }
        return sInstance.mFactory;


    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

    }
}
