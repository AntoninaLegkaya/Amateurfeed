package com.dbbest.amateurfeed.utils.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;
import com.dbbest.amateurfeed.utils.preferences.SharedPreferenceUtils;
import com.google.android.gms.maps.model.LatLng;

public final class LocationPreferences {

  static final String TAG = LocationPreferences.class.getName();
  private static final String FILE_NAME = "location";

  private static final String KEY_LAT = "LAT";
  private static final String KEY_LON = "LON";


  static void saveLastLocation(Context context, @Nullable LatLng latLng) {
    Log.i(TAG,
        " Save Location on Preferences Latitude: " + latLng.latitude + "\n" + "Longitude: "
            + latLng.longitude);
    SharedPreferences.Editor editor = getSharedPreferences(context).edit();
    SharedPreferenceUtils.putDouble(editor, KEY_LAT, latLng == null ? 0d : latLng.latitude);
    SharedPreferenceUtils.putDouble(editor, KEY_LON, latLng == null ? 0d : latLng.longitude);
    editor.apply();
  }


  public static LatLng readLastLocation(Context context) {
    double lat = SharedPreferenceUtils.getDouble(getSharedPreferences(context), KEY_LAT, 0d);
    double lon = SharedPreferenceUtils.getDouble(getSharedPreferences(context), KEY_LON, 0d);
    return new LatLng(lat, lon);
  }


  private static SharedPreferences getSharedPreferences(Context context) {
    return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
  }

}