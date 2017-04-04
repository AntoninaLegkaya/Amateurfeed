package com.dbbest.amateurfeed.app.azur;

import android.content.Context;
import android.net.Uri;
import android.os.ResultReceiver;
import com.dbbest.amateurfeed.app.azur.preferences.CloudPreferences;

public abstract class Storage {

  protected Context context;

  protected CloudPreferences prefs;

  public Storage() {
    prefs = new CloudPreferences();
  }

  public abstract String uploadToStorage(Uri file_path, ResultReceiver receiver);
}