package com.dbbest.amateurfeed.utils.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.dbbest.amateurfeed.utils.SharedPreferenceUtils;

/**
 * Created by antonina on 19.01.17.
 */

public final class LocationPreferences {

    private static final String FILE_NAME = "location";

    private static final String KEY_LAT = "LAT";
    private static final String KEY_LON = "LON";


    static void saveLastLocation(Context context, @Nullable LatLng latLng) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        SharedPreferenceUtils.putDouble(editor, KEY_LAT, latLng == null ? 0d : latLng.latitude());
        SharedPreferenceUtils.putDouble(editor, KEY_LON, latLng == null ? 0d : latLng.longitude());
        editor.apply();
    }


    static LatLng readLastLocation(Context context) {
        double lat = SharedPreferenceUtils.getDouble(getSharedPreferences(context), KEY_LAT, 0d);
        double lon = SharedPreferenceUtils.getDouble(getSharedPreferences(context), KEY_LON, 0d);
        return new LatLng(lat, lon);
    }


    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

}