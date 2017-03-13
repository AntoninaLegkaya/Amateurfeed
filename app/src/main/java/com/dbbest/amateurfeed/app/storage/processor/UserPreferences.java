package com.dbbest.amateurfeed.app.storage.processor;

import android.content.Context;
import android.content.SharedPreferences;

import com.dbbest.amateurfeed.App;

public class UserPreferences {

  private static final String PREFS_NAME = "UserPrefs";


  private static SharedPreferences settings;
  private static SharedPreferences.Editor editor;
  private static final String FULL_NAME_KEY = "userName";
  private static final String EMAIL_KEY = "email";
  private static final String IMAGE_KEY = "image";
  private static final String SKYPE_KEY = "skype";
  private static final String ADDRESS_KEY = "address";
  private static final String JOB_KEY = "job";
  private static final String PHONE_KEY = "phone";

  private static final String OK_KEY = "OK_KEY";


  private static SharedPreferences preferences() {
    if (settings == null) {
      settings = App.instance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
      editor = settings.edit();
      return settings;
    }
    return settings;


  }


  public static void setCredentials(String fullName, String email, String image, String skype,
      String address, String job, String phone) {

    preferences();
    editor.putString(FULL_NAME_KEY, fullName);
    editor.putString(EMAIL_KEY, email);
    editor.putString(IMAGE_KEY, image);
    editor.putString(SKYPE_KEY, skype);
    editor.putString(ADDRESS_KEY, address);
    editor.putString(JOB_KEY, job);
    editor.putString(PHONE_KEY, phone);
    editor.commit();
  }

  private static boolean isEmpty(String input) {
    return (input == null || input.trim().length() == 0);
  }

  public static String getFullName() {
    return preferences().getString(FULL_NAME_KEY, "");
  }

  public static String getEmail() {
    return preferences().getString(EMAIL_KEY, "");
  }

  public static String getImage() {
    return preferences().getString(IMAGE_KEY, "");
  }

  public static String getSkype() {
    return preferences().getString(SKYPE_KEY, "");
  }

  public static String getAddress() {
    return preferences().getString(ADDRESS_KEY, "");
  }

  public static String getJob() {
    return preferences().getString(JOB_KEY, "");
  }

  public static String getPhone() {
    return preferences().getString(PHONE_KEY, "");
  }


  @Override
  public String toString() {

    return "User preferences: " +
        "\nFull Name: " + getFullName() +
        "\nImage: " + getImage() +
        "\nEmail: " + getEmail() +
        "\nPhone: " + getPhone();
  }
}
