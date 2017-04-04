package com.dbbest.amateurfeed.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AmateurfeedSyncService extends Service {

  private static final Object syncAdapterLock = new Object();
  private static AmateurfeedSyncAdapter amateurfeedSyncAdapter = null;

  @Override
  public void onCreate() {
    synchronized (syncAdapterLock) {
      if (amateurfeedSyncAdapter == null) {
        amateurfeedSyncAdapter = new AmateurfeedSyncAdapter(getApplicationContext(), true);
      }
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return amateurfeedSyncAdapter.getSyncAdapterBinder();
  }
}