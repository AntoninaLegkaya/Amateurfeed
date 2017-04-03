package com.dbbest.amateurfeed.utils.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import com.dbbest.amateurfeed.App;

public class ImagePreferences {

  private static final String KEY_IMAGE_PATH = "IMAGE_PATH";
  private static final String PREF_IMAGE_PATH = ".img";
  private SharedPreferences settings;

  public final String getValue() {
    return preferences().getString(KEY_IMAGE_PATH, "");
  }

  public final boolean putValue(String value) {
    value = value == null ? "" : value;
    SharedPreferences.Editor editor = preferences().edit();
    editor.putString(KEY_IMAGE_PATH, value);
    return editor.commit();
  }

  private SharedPreferences preferences() {
    if (settings == null) {
      settings = App.instance().getSharedPreferences(PREF_IMAGE_PATH, Context.MODE_PRIVATE);
      return settings;
    }
    return settings;
  }


}
