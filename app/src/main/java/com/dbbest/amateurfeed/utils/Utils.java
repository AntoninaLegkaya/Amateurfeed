package com.dbbest.amateurfeed.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.Time;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.google.android.gms.maps.model.LatLng;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

  private static final int MY_PERMISSIONS_REQUEST_STORAGE = 123;
  private static final String TAG = Utils.class.getName();
  private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
          "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

  private static final Pattern NAME_PATTERN = Pattern.compile(
      "^[a-zA-Z0-9а-яА-Я ёЁ]+$"

  );

  //            (\B#\w)\w+
  private static final Pattern PHONE_PATTERN = Pattern.compile(
      "^(" +
          "(\\+3|8|\\+7|)[\\- ]?)" +
          "?(" +
          "\\(?" +
          "\\d{3}" +
          "\\)" +
          "?[\\- ]?" +
          ")" +
          "?[\\d\\- ]" +
          "{7,10}" +
          "$"
  );
  private static final Pattern TAG_PATTERN = Pattern.compile("(\\B#\\w)\\w+");

  public static String[] getTagsPattern(String input) {
    StringBuilder buffer = new StringBuilder();
    String[] strLines = input.split("\n");
    for (String line : strLines) {
      Matcher matcher = TAG_PATTERN.matcher(line);
      while (matcher.find()) {
        buffer.append(matcher.group().substring(1)).append(" ");
      }
    }
    return buffer.toString().split(" ");
  }

  public static boolean isEmailValid(String email) {
    return EMAIL_PATTERN.matcher(email).matches();
  }

  public static boolean isPasswordLengthValid(String password) {
    return (password.length() >= 6);
  }

  public static boolean isPasswordValid(String password) {

    return (password != null && !password.equals(""));
  }

  public static boolean isFullNameValid(String firstName) {
    return NAME_PATTERN.matcher(firstName).matches();
  }

  public static boolean isPhoneValid(String phone) {
    return PHONE_PATTERN.matcher(phone).matches();
  }

  public static long getLongFromString(String currentDate) {
//    "2010-10-15T09:27:37";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    Date parseDate = null;
    try {
      parseDate = format.parse(currentDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return parseDate != null ? parseDate.getTime() : 0;
  }

  public static String getFriendlyDayString(Context context, long dateInMillis, boolean displayLongToday) {
    // The day string for forecast uses the following logic:
    // For today: "Today, June 8"
    // For tomorrow:  "Tomorrow"
    // For the next 5 days: "Wednesday" (just the day name)
    // For all days after that: "Mon Jun 8"

    Time time = new Time();
    time.setToNow();
    long currentTime = System.currentTimeMillis();
    int julianDay = Time.getJulianDay(dateInMillis, time.gmtoff);
    int currentJulianDay = Time.getJulianDay(currentTime, time.gmtoff);

    // If the date we're building the String for is today's date, the format
    // is "Today, June 24"
    if (displayLongToday && julianDay == currentJulianDay) {
//    if (displayLongToday && dateInMillis==getTodayLongDate()) {
      String today = context.getString(R.string.today);
      int formatId = R.string.format_full_friendly_date;
      return String.format(context.getString(formatId), today, getFormattedMonthDay(dateInMillis));
    } else {
      // Otherwise, use the form "Mon Jun 3"
      SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm", Locale.US);
      return shortenedDateFormat.format(dateInMillis);
    }
  }

  public static long getTodayLongDate() {
    Calendar currDate = Calendar.getInstance();
    currDate.set(Calendar.HOUR_OF_DAY, 0);
    currDate.set(Calendar.MINUTE, 0);
    return currDate.getTimeInMillis();
  }

  public static String formatTagName(Context context, String tag) {
    int formatId = R.string.format_tag;
    return String.format(context.getString(formatId), tag);
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public static boolean checkPermission(final Context context) {
    int currentAPIVersion = Build.VERSION.SDK_INT;
    if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(context, "android.permission.READ_EXTERNAL_STORAGE")
          != PackageManager.PERMISSION_GRANTED
          || ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE")
          != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
            "android.permission.READ_EXTERNAL_STORAGE") || ActivityCompat
            .shouldShowRequestPermissionRationale((Activity) context,
                "android.permission.WRITE_EXTERNAL_STORAGE")) {
          AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
          alertBuilder.setCancelable(true);
          alertBuilder.setTitle("Permission necessary");
          alertBuilder.setMessage("External storage permission is necessary");
          alertBuilder
              .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                  ActivityCompat.requestPermissions((Activity) context,
                      new String[]{"android.permission.READ_EXTERNAL_STORAGE",
                          "android.permission.WRITE_EXTERNAL_STORAGE"},
                      MY_PERMISSIONS_REQUEST_STORAGE);
                }
              });
          AlertDialog alert = alertBuilder.create();
          alert.show();
        } else {
          ActivityCompat.requestPermissions((Activity) context,
              new String[]{"android.permission.READ_EXTERNAL_STORAGE",
                  "android.permission.WRITE_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_STORAGE);
        }
        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
  }

  public static String getNameFromPath(Uri uri) {
    String path = uri.getPath();
    if (path == null || path.length() == 0) {
      return "";
    }
    int mid = path.lastIndexOf("/");
    if (mid == -1) {
      return path;
    }
    return path.substring(mid + 1);
  }

  public static String getImageMimeType(String path) {

    String ext = getPathExtension(path);
    if ("jpg".equals(ext) || "jpe".equals(ext)) {
      ext = "jpeg";
    }
    return "image/" + ext;
  }

  public static boolean externalMemoryAvailable() {
    if (Environment.isExternalStorageRemovable()) {
      //device support sd card. We need to check sd card availability.
      String state = Environment.getExternalStorageState();
      return state.equals(Environment.MEDIA_MOUNTED) || state.equals(
          Environment.MEDIA_MOUNTED_READ_ONLY);
    } else {
      //device not support sd card.
      return false;
    }
  }

  public static void copyFile(File sourceFile, File destFile) throws IOException {
    if (!sourceFile.exists()) {
      return;
    }
    FileChannel source = new FileInputStream(sourceFile).getChannel();
    FileChannel destination = new FileOutputStream(destFile).getChannel();
    if (source != null) {
      destination.transferFrom(source, 0, source.size());
    }
    if (source != null) {
      source.close();
    }
    destination.close();
  }

  public static String getCurrentTime() {
    Calendar c = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    return format.format(c.getTime());
  }

  public static boolean isValid(LatLng location) {
    return location.latitude != 0.0d && location.longitude != 0.0d;
  }

  public static boolean checkNotificationPref() {
    SharedPreferences prefs =
        getDefaultSharedPreferencesMultiProcess(App.instance().getApplicationContext());
    String displayNotificationsKey = App.instance().getApplicationContext()
        .getString(R.string.pref_enable_notifications_key);
    boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
        Boolean.parseBoolean(App.instance().getApplicationContext()
            .getString(R.string.pref_enable_notifications_default)));
    Log.i(TAG, "Check notification push message: " + displayNotifications);
    return displayNotifications;
  }

  public static SharedPreferences getDefaultSharedPreferencesMultiProcess(
      Context context) {
    return context.getSharedPreferences(
        context.getPackageName() + "_preferences",
        Context.MODE_MULTI_PROCESS);
  }

  private static String getFormattedMonthDay(long dateInMillis) {
    SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMM dd HH:mm", Locale.US);
    return monthDayFormat.format(dateInMillis);
  }

  private static String getPathExtension(String path) {

    if (path == null) {
      return null;
    }
    int mid = path.lastIndexOf(".");
    if (mid == -1) {
      return null;
    }
    String ext = path.substring(mid + 1, path.length());
    return ext.toLowerCase();
  }
}