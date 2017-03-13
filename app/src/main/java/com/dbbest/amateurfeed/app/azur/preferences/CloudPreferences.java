package com.dbbest.amateurfeed.app.azur.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import com.dbbest.amateurfeed.App;

public class CloudPreferences {

  static final String PREFS_NAME = "BlobberPrefs";
  static final String ACCT_NAME_KEY = "acctName";
  static final String ACCESS_KEY = "acctKey";
  static final String CONTAINER_KEY = "container";
  static final String STORAGE_URL = "url";
  static final String OK_KEY = "OK_KEY";
  private SharedPreferences settings;
  private SharedPreferences.Editor editor;


  public CloudPreferences() {

  }

  private static boolean isEmpty(String input) {
    return (input == null || input.trim().length() == 0);
  }

  private String readNameKey() {
    if (preferences() != null) {
      return preferences().getString(ACCT_NAME_KEY, null);
    }
    return null;
  }

  public void writeNameKey(String value) {
    preferences().edit().putString(ACCT_NAME_KEY, value).apply();
  }

  public String readAccessKey() {
    if (preferences() != null) {
      return preferences().getString(ACCESS_KEY, null);
    }
    return null;
  }

  public void writeAccessKey(String value) {
    preferences().edit().putString(ACCESS_KEY, value).apply();
  }

  public String readContainerKey() {
    if (preferences() != null) {
      return preferences().getString(CONTAINER_KEY, null);
    }
    return null;
  }

  public void writeContainerName(String value) {
    preferences().edit().putString(CONTAINER_KEY, value).apply();
  }

  private String readStorageUrl() {
    if (preferences() != null) {
      return preferences().getString(STORAGE_URL, null);
    }
    return null;
  }

  public void writeStorageUrl(String value) {
    preferences().edit().putString(STORAGE_URL, value).apply();
  }

  private SharedPreferences preferences() {
    if (settings == null) {
      settings = App.instance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
      editor = settings.edit();
      return settings;
    }
    return settings;


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

    preferences();
    editor.putString(ACCT_NAME_KEY, acctName);
    editor.putString(ACCESS_KEY, acctKey);
    editor.putString(CONTAINER_KEY, container);
    editor.putString(STORAGE_URL, url);

    boolean notFilled =
        isEmpty(acctName) ||
            isEmpty(acctKey) ||
            isEmpty(container);

    editor.putBoolean(OK_KEY, !notFilled);
    editor.commit();
  }

  @Override
  public String toString() {

    return "Cloud preferences: " +
        "\nAccount Name: " + getAccountName() +
        "\nAccesskey: " + getAccessKey() +
        "\nContainer: " + getContainer() +
        "\nUrl: " + getStorageUrl();
  }
}