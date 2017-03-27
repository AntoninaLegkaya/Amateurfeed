package com.dbbest.amateurfeed.data.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.UpdateNewsCommand;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.utils.Utils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AmateurfeedSyncAdapter extends AbstractThreadedSyncAdapter implements
    CommandResultReceiver.CommandListener {

  // Interval at which to sync with the news, in seconds.
  // 60 seconds (1 minute) * 180 = 3 hours
  public static final int SYNC_INTERVAL = 60;
  public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
  final public static int PREVIEW_STATUS_OK = 0;
  final public static int PREVIEW_STATUS_SERVER_DOWN = 1;
  final public static int PREVIEW_STATUS_SERVER_INVALID = 2;
  final public static int PREVIEW_STATUS_UNKNOWN = 3;
  final public static int PREVIEW_STATUS_INVALID = 4;
  private static final String TAG = AmateurfeedSyncAdapter.class.getSimpleName();
  private static final int NEWS_NOTIFICATION_ID = 3004;
  private static final int CODE_GET_NEWS = 0;
  private CommandResultReceiver mResultReceiver;


  public AmateurfeedSyncAdapter(Context context, boolean autoInitialize) {
    super(context, autoInitialize);
  }

  /**
   * Helper method to schedule the sync adapter periodic execution
   */
  public static void configurePeriodicSync(Context context, int syncInterval,
      int flexTime) {
    Account account = getSyncAccount(context);
    String authority = context.getString(R.string.content_authority);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      // we can enable inexact timers in our periodic sync
      SyncRequest request = new SyncRequest.Builder().
          syncPeriodic(syncInterval, flexTime).
          setSyncAdapter(account, authority).
          setExtras(new Bundle()).build();
      ContentResolver.requestSync(request);
    } else {
      ContentResolver.addPeriodicSync(account,
          authority, new Bundle(), syncInterval);
    }
  }

  /**
   * Helper method to have the sync adapter sync immediately
   *
   * @param context The context used to access the account service
   */
  public static void syncImmediately(Context context) {
    Bundle bundle = new Bundle();
    bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
    bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
    ContentResolver.requestSync(getSyncAccount(context),
        context.getString(R.string.content_authority), bundle);
    Log.i(TAG, "Sync immediately. App get Push notification ");
  }

  /**
   * Helper method to get the fake account to be used with SyncAdapter, or make a new one
   * if the fake account doesn't exist yet.  If we make a new account, we call the
   * onAccountCreated method so we can initialize things.
   *
   * @param context The context used to access the account service
   * @return a fake account.
   */
  public static Account getSyncAccount(Context context) {
    // Get an instance of the Android account manager
    AccountManager accountManager =
        (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

    // Create the account type and default account
    Account newAccount = new Account(
        context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

    // If the password doesn't exist, the account doesn't exist
    if (null == accountManager.getPassword(newAccount)) {
      Log.i(TAG, "Password: " + accountManager.getPassword(newAccount));
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
      if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
        return null;
      }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
      onAccountCreated(newAccount, context);
    }
    return newAccount;
  }

  private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
    AmateurfeedSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
    Log.i(TAG, "Account: " + newAccount.toString());
    ContentResolver
        .setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
    syncImmediately(context);
  }

  public static void initializeSyncAdapter(Context context) {
    getSyncAccount(context);
  }

  @Override
  public void onPerformSync(Account account, Bundle extras, String authority,
      ContentProviderClient provider, SyncResult syncResult) {
    boolean displayNotifications = Utils.checkNotificationPref();
    if (displayNotifications) {
      Uri previewListUri = FeedContract.PreviewEntry.CONTENT_URI;
      Cursor cursor = App.instance().getContentResolver().query(
          previewListUri,
          null,
          null,
          null,
          null
      );
      if (cursor.moveToFirst()) {
        Command command = new UpdateNewsCommand(0, cursor.getCount());
        command.send(CODE_GET_NEWS, mResultReceiver);
      }
      Log.i(TAG, "Thread transfer data Server<--->Device");
    } else {
      Log.i(TAG, App.instance().getApplicationContext().getString(R.string.notifier_push));
    }
  }

  @Override
  public void onSuccess(int code, Bundle data) {
    Log.i(TAG, "Updated data by sync");
    ((Callback) getContext()).refreshFeed();
  }

  @Override
  public void onFail(int code, Bundle data) {
    Log.e(TAG, "Not Updated data by sync");
  }

  @Override
  public void onProgress(int code, Bundle data, int progress) {
  }


  @Retention(RetentionPolicy.SOURCE)
  @IntDef({PREVIEW_STATUS_OK, PREVIEW_STATUS_SERVER_DOWN, PREVIEW_STATUS_SERVER_INVALID,
      PREVIEW_STATUS_UNKNOWN, PREVIEW_STATUS_INVALID})
  public @interface PreviewStatus {

  }

  public interface Callback {

    void refreshFeed();
  }
}