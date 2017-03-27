package com.dbbest.amateurfeed.data.sync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Calendar;


public class TimerReceiverSyncInterval extends BroadcastReceiver {

  private static final String TAG = TimerReceiverSyncInterval.class.getName();

  public static void scheduleAlarms(Context paramContext) {

    Calendar calendar = Calendar.getInstance();
    AlarmManager localAlarmManager = (AlarmManager) paramContext
        .getSystemService(Context.ALARM_SERVICE);
    PendingIntent localPendingIntent = PendingIntent.getService(paramContext, 0,
        new Intent(paramContext, NotificationServiceSyncInterval.class),
        PendingIntent.FLAG_UPDATE_CURRENT);

    localAlarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
        60000, localPendingIntent);

    Log.i(TAG, "Sync------");

  }

  @Override
  public void onReceive(Context context, Intent intent) {

    Log.d(TAG, "Sync OnReceive--");

    scheduleAlarms(context);
    context.startService(new Intent(context, NotificationServiceSyncInterval.class));

  }
}