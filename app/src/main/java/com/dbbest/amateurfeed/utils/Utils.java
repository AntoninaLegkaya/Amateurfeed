package com.dbbest.amateurfeed.utils;

import android.content.Context;
import android.text.format.Time;

import com.dbbest.amateurfeed.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by antonina on 19.01.17.
 */

public class Utils {
    public static final String TAG_LOG = "Log";
    public static final String DATE_FORMAT = "yyyyMMdd";
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

    public static long getLongFromString(String currentDate) throws ParseException {


//    "2010-10-15T09:27:37";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date parseDate = format.parse(currentDate);
        return parseDate.getTime();


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
            String today = context.getString(R.string.today);
            int formatId = R.string.format_full_friendly_date;
            return String.format(context.getString(formatId), today, getFormattedMonthDay(context, dateInMillis));
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

    public static String getFormattedMonthDay(Context context, long dateInMillis) {
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
}
