package com.dbbest.amateurfeed.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AmateurfeedSyncService extends Service {

  private static final Object sSyncAdapterLock = new Object();
  private static AmateurfeedSyncAdapter sAmateurfeedSyncAdapter = null;

  @Override
  public void onCreate() {
    synchronized (sSyncAdapterLock) {
      if (sAmateurfeedSyncAdapter == null) {
        sAmateurfeedSyncAdapter = new AmateurfeedSyncAdapter(getApplicationContext(), true);
      }
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return sAmateurfeedSyncAdapter.getSyncAdapterBinder();
  }
}