package com.dbbest.amateurfeed.utils.preferences;

import android.content.SharedPreferences;


public class SharedPreferenceUtils {


  private SharedPreferenceUtils() {

  }

  public static SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit,
      final String key, final double value) {
    return edit.putLong(key, Double.doubleToRawLongBits(value));
  }

  public static double getDouble(final SharedPreferences prefs, final String key,
      final double defaultValue) {
    return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
  }
}
