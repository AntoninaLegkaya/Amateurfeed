package com.dbbest.amateurfeed.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.Time;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

  public static final String TAG_LOG_LOAD_NEW_DATA = "Get news";
  public static final String DATE_FORMAT = "yyyyMMdd";
  public static final int MY_PERMISSIONS_REQUEST_STORAGE = 123;
  private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
          "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
  private static final Pattern PASSWORD_PATTERN = Pattern.compile(
      "(" +
          "(?=.*\\d)" +

          "(?=.*[a-z])" +

          "." +

          "{6,20}" +
          ")"
  );
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

  public static final String[] getTagsPattern(String input) {
    StringBuffer buffer = new StringBuffer();
    String[] strLines = input.split("\n");
    for (String line : strLines) {
      Matcher matcher = TAG_PATTERN.matcher(line);
      while (matcher.find()) {
        buffer.append(matcher.group().substring(1) + " ");
      }
    }
    return buffer.toString().split(" ");
  }

  public static boolean isEmailValid(String email) {
    return EMAIL_PATTERN.matcher(email).matches();
  }

  public static boolean isPasswordLengthValid(String password) {
    return (password.length() > 6);
  }

  public static boolean isPasswordValid(String password) {
//        return PASSWORD_PATTERN.matcher(password).matches();
    return true;
  }

  public static boolean isFullNameValid(String firstName) {
    return NAME_PATTERN.matcher(firstName).matches();
  }

  public static boolean isPhoneValid(String phone) {
    return PHONE_PATTERN.matcher(phone).matches();
  }

  public static long getLongFromString(String currentDate) {
//    "2010-10-15T09:27:37";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    Date parseDate = null;
    try {
      parseDate = format.parse(currentDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return parseDate.getTime();
  }

  public static String getFriendlyDayString(Context context, long dateInMillis,
      boolean displayLongToday) {
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
      String today = context.getString(R.string.today);
      int formatId = R.string.format_full_friendly_date;
      return String.format(context.getString(formatId), today, getFormattedMonthDay(dateInMillis));
    }
//        else if (julianDay < currentJulianDay - 7) {
//            // If the input date is less than a week in the pass, just return the day name.
//            return getDayName(context, dateInMillis);
//        }
    else {
      // Otherwise, use the form "Mon Jun 3"
      SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm");
      return shortenedDateFormat.format(dateInMillis);
    }
  }

  public static String getFormattedMonthDay(long dateInMillis) {
    Time time = new Time();
    time.setToNow();
    SimpleDateFormat dbDateFormat = new SimpleDateFormat(Utils.DATE_FORMAT);
    SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM dd HH:mm");
    String monthDayString = monthDayFormat.format(dateInMillis);
    return monthDayString;
  }

  public static String getDayName(Context context, long dateInMillis) {
    // If the date is today, return the localized version of "Today" instead of the actual
    // day name.

    Time t = new Time();
    t.setToNow();
    int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
    int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
    if (julianDay == currentJulianDay) {
      return context.getString(R.string.today);
    } else if (julianDay == currentJulianDay - 1) {
      return context.getString(R.string.yesterday);
    } else {
      Time time = new Time();
      time.setToNow();
      // Otherwise, the format is just the day of the week (e.g "Wednesday".
      SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
      return dayFormat.format(dateInMillis);
    }
  }

  public static long getTodayLongDate() {
    Calendar currDate = Calendar.getInstance();
    currDate.set(Calendar.HOUR_OF_DAY, 0);
    currDate.set(Calendar.MINUTE, 0);
    return currDate.getTimeInMillis();
  }

  public static boolean isAddressValid(String address) {
    return false;
  }

  public static boolean isDeviceIdValid(String deviceId) {
    return false;
  }

  public static boolean isOsTypeValid(String osType) {
    return false;
  }

  public static boolean isDeviceTokenValid(String deviceToken) {
    return false;
  }

  public static long getLongData(String string) {
    return -1;
  }

  public static String foramatTagName(Context context, String tag) {
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

  public static String getPathExtension(String path) {

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
    if (destination != null && source != null) {
      destination.transferFrom(source, 0, source.size());
    }
    if (source != null) {
      source.close();
    }
    if (destination != null) {
      destination.close();
    }
  }


  public static String getCurrentTime() {
    Calendar c = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    return format.format(c.getTime());
  }

  public static boolean isValid(LatLng location) {
    return location.latitude != 0.0d && location.longitude != 0.0d;
  }
}