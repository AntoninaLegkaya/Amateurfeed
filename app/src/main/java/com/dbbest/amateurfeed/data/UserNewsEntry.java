package com.dbbest.amateurfeed.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class UserNewsEntry implements BaseColumns {

  public static final String AUTHORITY = FeedContract.AUTHORITY;
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
  public static final String PATH_USER_NEWS = "user_news";
  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_USER_NEWS;
  public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_USER_NEWS;
  public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_NEWS).build();
  public static final String TABLE_NAME = "user_news";
  public static final String COLUMN_TITLE = "title";
  public static final String COLUMN_UPDATE_DATE = "update_date";
  public static final String COLUMN_STATUS = "status";
  public static final String COLUMN_IMAGE = "image";
  public static final String COLUMN_LIKES = "likes";
  public static final int PREVIEW_ID_PATH_POSITION = 1;
  public static final String[] NEWS_COLUMNS = {
      TABLE_NAME + "." + _ID,
      COLUMN_TITLE,
      UserNewsEntry.COLUMN_UPDATE_DATE,
      UserNewsEntry.COLUMN_STATUS,
      UserNewsEntry.COLUMN_IMAGE,
      UserNewsEntry.COLUMN_LIKES,
  };
  private static final String PATH_USER_NEWS_ID = "/" + PATH_USER_NEWS + "/";
  public static final Uri CONTENT_ID_URI_BASE = Uri.parse(BASE_CONTENT_URI + PATH_USER_NEWS_ID);

  public static Uri buildUserNewsUri(long id) {
    return ContentUris.withAppendedId(com.dbbest.amateurfeed.data.UserNewsEntry.CONTENT_URI, id);
  }

  public static Uri buildGetNewsById(long id) {
    return ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
  }
}