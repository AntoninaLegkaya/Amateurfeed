package com.dbbest.amateurfeed.data.sync;

import static com.dbbest.amateurfeed.data.sync.AmateurfeedSyncAdapter.syncImmediately;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.dbbest.amateurfeed.App;

public class NotificationServiceSyncInterval extends IntentService {

  private final String TAG = NotificationServiceSyncInterval.class.getName();


  public NotificationServiceSyncInterval() {
    super("Sync Tracker Online");

  }

  public NotificationServiceSyncInterval(String paramString) {
    super(paramString);

  }

  public static void callSync() {

    try {
      syncImmediately(App.instance().getApplicationContext());

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  protected void onHandleIntent(Intent intent) {

    Log.d(TAG, "Sync Handler call");
    // todo
    callSync();

  }
}