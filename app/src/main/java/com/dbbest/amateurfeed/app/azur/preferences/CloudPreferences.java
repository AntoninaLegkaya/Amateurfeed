package com.dbbest.amateurfeed.app.azur.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import com.dbbest.amateurfeed.App;

public class CloudPreferences {

  private static final String PREFS_NAME = "BlobberPrefs";
  private static final String ACCT_NAME_KEY = "acctName";
  private static final String ACCESS_KEY = "acctKey";
  private static final String CONTAINER_KEY = "container";
  private static final String STORAGE_URL = "url";
  private static final String OK_KEY = "OK_KEY";
  private SharedPreferences settings;
  private SharedPreferences.Editor editor;

  private static boolean isEmpty(String input) {
    return (input == null || input.trim().length() == 0);
  }

  public CloudPreferences() {
  }

  @Override
  public String toString() {

    return "Cloud preferences: " +
        "\nAccount Name: " + getAccountName() +
        "\nAccesskey: " + getAccessKey() +
        "\nContainer: " + getContainer() +
        "\nUrl: " + getStorageUrl();
  }

  public String getAccountName() {
    return preferences().getString(ACCT_NAME_KEY, "");
  }

  public String getAccessKey() {
    return preferences().getString(ACCESS_KEY, "");
  }

  public String getContainer() {
    return preferences().getString(CONTAINER_KEY, "");
  }

  public String getStorageUrl() {
    return preferences().getString(STORAGE_URL, "");
  }

  public void setCredentials(String acctName, String acctKey, String container, String url) {

    editor = preferences().edit();
    editor.putString(ACCT_NAME_KEY, acctName);
    editor.putString(ACCESS_KEY, acctKey);
    editor.putString(CONTAINER_KEY, container);
    editor.putString(STORAGE_URL, url);

    boolean notFilled =
        isEmpty(acctName) ||
            isEmpty(acctKey) ||
            isEmpty(container);

    editor.putBoolean(OK_KEY, !notFilled);
    editor.apply();
  }


  private SharedPreferences preferences() {
    if (settings == null) {
      settings = App.instance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

      return settings;
    }
    return settings;
  }
}