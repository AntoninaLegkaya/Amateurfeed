package com.dbbest.amateurfeed.data.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.Time;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.app.net.response.NewsPreviewResponseModel;
import com.dbbest.amateurfeed.app.net.response.NewsResponseModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;

public class AmateurfeedSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = AmateurfeedSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;
    public static final String ACTION_DATA_UPDATED = "com.example.antonina.less12.app.ACTION_DATA_UPDATED";


    private NewsResponseModel responceNews;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PREVIEW_STATUS_OK, PREVIEW_STATUS_SERVER_DOWN, PREVIEW_STATUS_SERVER_INVALID,
            PREVIEW_STATUS_UNKNOWN, PREVIEW_STATUS_INVALID})
    public @interface PreviewStatus {
    }

    final public static int PREVIEW_STATUS_OK = 0;
    final public static int PREVIEW_STATUS_SERVER_DOWN = 1;
    final public static int PREVIEW_STATUS_SERVER_INVALID = 2;
    final public static int PREVIEW_STATUS_UNKNOWN = 3;
    final public static int PREVIEW_STATUS_INVALID = 4;


    public AmateurfeedSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");

       //TODO Do Retrofit Request to Get new Data
//        if (response != null) {
//            getFromJsonRetrofit(response);
//
//        }


    }


    public void getWeatherDataFromJsonRetrofit(NewsResponseModel newsFeed,
                                               String locationSetting) {


        // do we have en error
//        if (null != newsFeed.getCod()) {
//
//            int errorCode = Integer.parseInt(newsFeed.getCod());
//            Log.d(LOG_TAG, "Response code from HTTP Connection: " + errorCode);
//            switch (errorCode) {
//
//                case HttpURLConnection.HTTP_OK:
//                    break;
//                case HttpURLConnection.HTTP_NOT_FOUND:
//                    setLocationStatus(getContext(), LOCATION_STATUS_INVALID);
//                    return;
//                case HttpURLConnection.HTTP_BAD_GATEWAY:
//                    setLocationStatus(getContext(), LOCATION_STATUS_INVALID);
//                    return;
//                default:
//                    setLocationStatus(getContext(), LOCATION_STATUS_SERVER_DOWN);
//                    return;
//            }
//
//        }
//
//
//
//
//        java.util.List<List> forecastWeatherList = newsFeed.getList();
//        int i = 0;
//        for (List day : forecastWeatherList) {
//            // These are the values that will be collected.
//
//
//            ContentValues weatherValues = new ContentValues();
//
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationId);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateTime);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, humidity);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, pressure);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, high);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, low);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, description);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherId);
//
//            cVVector.add(weatherValues);
//            i = i + 1;
//        }
//
//        // add to database
//        if (cVVector.size() > 0) {
//            ContentValues[] cvArray = new ContentValues[cVVector.size()];
//            cVVector.toArray(cvArray);
//            getContext().getContentResolver().bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, cvArray);
//
//            // delete old data so we don't build up an endless history
//            getContext().getContentResolver().delete(WeatherContract.WeatherEntry.CONTENT_URI,
//                    WeatherContract.WeatherEntry.COLUMN_DATE + " <= ?",
//                    new String[]{Long.toString(dayTime.setJulianDay(julianStartDay - 1))});
//        }
//
//        Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");


    }


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
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
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}