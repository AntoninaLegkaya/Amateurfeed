package com.dbbest.amateurfeed.utils.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import com.dbbest.amateurfeed.App;

public class UserPreferences {

  private final String PREFS_NAME = "UserPrefs";
  private final String OK_KEY = "OK_KEY";
  private final String FULL_NAME_KEY = "userName";
  private final String EMAIL_KEY = "email";
  private final String IMAGE_KEY = "image";
  private final String SKYPE_KEY = "skype";
  private final String ADDRESS_KEY = "address";
  private final String JOB_KEY = "job";
  private final String PHONE_KEY = "phone";
  private SharedPreferences settings;
  private SharedPreferences.Editor editor;

  private boolean isEmpty(String input) {
    return (input == null || input.trim().length() == 0);
  }

  private SharedPreferences preferences() {
    if (settings == null) {
      settings = App.instance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
      editor = settings.edit();
      return settings;
    }
    return settings;
  }

  public void setCredentials(String fullName, String email, String image, String skype,
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

  public String getFullName() {
    return preferences().getString(FULL_NAME_KEY, "");
  }

  public String getEmail() {
    return preferences().getString(EMAIL_KEY, "");
  }

  public String getImage() {
    return preferences().getString(IMAGE_KEY, "");
  }

  public String getSkype() {
    return preferences().getString(SKYPE_KEY, "");
  }

  public String getAddress() {
    return preferences().getString(ADDRESS_KEY, "");
  }

  public String getJob() {
    return preferences().getString(JOB_KEY, "");
  }

  public String getPhone() {
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
