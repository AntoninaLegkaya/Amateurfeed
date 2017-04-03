package com.dbbest.amateurfeed.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class CreatorEntry implements BaseColumns {

  public static final int CREATOR_ID_PATH_POSITION = 1;
  public static final Uri CONTENT_URI = FeedContract.BASE_CONTENT_URI.buildUpon().appendPath(FeedContract.PATH_CREATOR)
      .build();
  public static final String CONTENT_TYPE =
      ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + FeedContract.AUTHORITY + "/" + FeedContract.PATH_CREATOR;
  public static final String CONTENT_ITEM_TYPE =
      ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + FeedContract.AUTHORITY + "/" + FeedContract.PATH_CREATOR;

  public static final String TABLE_NAME = "creator";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_IS_ADMIN = "is_admin";
  public static final String COLUMN_IMAGE = "image";
  private static final String SCHEME = "content://";
  private static final String PATH_CREATOR_ID = "/creator/";
  public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + FeedContract.AUTHORITY + PATH_CREATOR_ID);

  public static Uri buildCreatorUri(long id) {
    return ContentUris.withAppendedId(CONTENT_URI, id);
  }

  public static Uri buildCreatorUriById(long id) {
    return ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
  }


  public static long getIdFromUri(Uri uri) {

    return Long.parseLong(uri.getPathSegments().get(1));
  }

  public static Uri buildGetIdCreatorByAuthor(String author) {
    return CONTENT_ID_URI_BASE.buildUpon().appendPath(author).build();
  }

  public static String getAuthorFromUri(Uri uri) {
    return uri.getPathSegments().get(1);
  }
}
