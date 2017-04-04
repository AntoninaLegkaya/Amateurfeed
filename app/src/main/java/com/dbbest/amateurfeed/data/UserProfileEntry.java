package com.dbbest.amateurfeed.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class UserProfileEntry implements BaseColumns {

  public static final Uri CONTENT_URI =
      FeedContract.BASE_CONTENT_URI.buildUpon().appendPath(FeedContract.PATH_PROFILE).build();
  public static final String CONTENT_TYPE =
      ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + FeedContract.AUTHORITY + "/" + FeedContract.PATH_PROFILE;
  public static final String CONTENT_ITEM_TYPE =
      ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + FeedContract.AUTHORITY + "/" + FeedContract.PATH_PROFILE;

  public static final String TABLE_NAME = "profile_user";
  public static final String COLUMN_FULL_NAME = "full_name";
  public static final String COLUMN_EMAIL = "email";
  public static final String COLUMN_IMAGE = "image";
  public static final String COLUMN_SKYPE = "skype";
  public static final String COLUMN_ADDRESS = "address";
  public static final String COLUMN_JOB = "job";
  public static final String COLUMN_PHONE = "phone";

  public static Uri buildUserProfileUri(long id) {
    return ContentUris.withAppendedId(CONTENT_URI, id);
  }
}
