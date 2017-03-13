package com.dbbest.amateurfeed.app.azur;

import android.content.Context;
import android.net.Uri;
import com.dbbest.amateurfeed.app.azur.preferences.CloudPreferences;

public abstract class Storage {

  /**
   * All providers will have access to context
   */
  protected Context context;

  /**
   * All providers will have accesss to SharedPreferences
   */
  protected CloudPreferences prefs;

  public Storage(Context ctx) {
    context = ctx;
    prefs = new CloudPreferences();
  }


  public abstract String uploadToStorage(Uri file_path);


}