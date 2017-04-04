package com.dbbest.amateurfeed;

import android.support.multidex.MultiDexApplication;
import com.crashlytics.android.Crashlytics;
import com.dbbest.amateurfeed.app.net.retrofit.ApiFactory;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.Stetho;
import io.fabric.sdk.android.Fabric;


public class App extends MultiDexApplication {

  private static App sInstance;
  private static ApiFactory sApiFactory;

  public static App instance() {
    return sInstance;
  }

  public static ApiFactory getApiFactory() {
    if (sApiFactory == null) {
      sApiFactory = new ApiFactory();
    }
    return sInstance.sApiFactory;
  }


  @Override
  public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
    FacebookSdk.sdkInitialize(getApplicationContext());
    AppEventsLogger.activateApp(this);
    Stetho.initializeWithDefaults(this);
    sInstance = this;

  }
}
