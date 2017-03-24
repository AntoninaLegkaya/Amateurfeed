package com.dbbest.amateurfeed.data.sync;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;

public class ContinuousSyncService extends IntentService {

  private static final int DELAY = 1000; // ms

  public ContinuousSyncService() {
    super(ContinuousSyncService.class.getName());
  }

  public static PendingIntent getPendingIntent(@NonNull Context context) {
    Intent intent = new Intent(context, ContinuousSyncService.class);
    return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  private void scheduleNextStart(long delay) {
    ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).set(
        AlarmManager.ELAPSED_REALTIME,
        SystemClock.elapsedRealtime() + delay,
        getPendingIntent(this));
  }

  @Override
  protected void onHandleIntent(final Intent intent) {
    sync();
    scheduleNextStart(DELAY);
  }

  private void sync() {
    // Either use ContentResolver.requestSync()
    // Or just put the code from your SyncAdapter.onPerformSync() here
  }

}
