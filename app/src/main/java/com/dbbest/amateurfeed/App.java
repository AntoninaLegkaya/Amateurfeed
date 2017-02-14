package com.dbbest.amateurfeed;

import android.support.multidex.MultiDexApplication;

import com.dbbest.amateurfeed.app.net.retrofit.ApiFactory;
import com.dbbest.amateurfeed.utils.FactoryImpl;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.Stetho;



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
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Stetho.initializeWithDefaults(this);
        sInstance = this;

    }
}
