package com.dbbest.amateurfeed.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class TagEntry implements BaseColumns {

  public static final int TAG_ID_PATH_POSITION = 1;
  public static final Uri CONTENT_URI = FeedContract.BASE_CONTENT_URI.buildUpon().appendPath(FeedContract.PATH_TAG).build();
  public static final String CONTENT_TYPE =
      ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + FeedContract.AUTHORITY + "/" + FeedContract.PATH_TAG;
  public static final String CONTENT_ITEM_TYPE =
      ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + FeedContract.AUTHORITY + "/" + FeedContract.PATH_PROFILE;

  public static final String TABLE_NAME = "tag";
  public static final String COLUMN_TAG_ID = "tag_id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_PREVIEW_ID = "preview_id";
  private static final String SCHEME = "content://";
  private static final String PATH_TAG_ID = "/tag/";
  public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + FeedContract.AUTHORITY + PATH_TAG_ID);

  public static Uri buildTagUri(long id) {
    return ContentUris.withAppendedId(CONTENT_URI, id);
  }

  public static Uri buildTagUriById(long id) {
    return ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
  }

  public static Uri getTagsListById(long id) {

    return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
  }
}
