package com.dbbest.amateurfeed.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class PreviewEntry implements BaseColumns {

  public static final int PREVIEW_ID_PATH_POSITION = 1;

  public static final String TABLE_NAME = "preview";
  public static final String COLUMN_TITLE = "title";
  public static final String COLUMN_TEXT = "text";
  public static final String COLUMN_LIKES = "likes";
  public static final String COLUMN_IS_LIKE = "is_like";
  public static final String COLUMN_AUTHOR = "author";
  public static final String COLUMN_AUTHOR_IMAGE = "author_image";
  public static final String COLUMN_CREATE_DATE = "create_date";
  public static final String COLUMN_IMAGE = "image";
  public static final String COLUMN_IS_MY = "is_my";
  public static final String[] DEFAULT_PROJECTION = new String[]{
      TABLE_NAME + "." + _ID,
      COLUMN_TITLE,
      COLUMN_TEXT,
      COLUMN_LIKES,
      COLUMN_IS_LIKE,
      COLUMN_AUTHOR,
      COLUMN_AUTHOR_IMAGE,
      COLUMN_CREATE_DATE,
      COLUMN_IMAGE,
      COLUMN_IS_MY

  };
  private static final String SCHEME = "content://";
  private static final String PATH_PREVIEW = "/preview";
  public static final Uri CONTENT_URI = Uri.parse(SCHEME + FeedContract.AUTHORITY + PATH_PREVIEW);
  public static final String CONTENT_TYPE =
      ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + FeedContract.AUTHORITY + "/" + PATH_PREVIEW;
  public static final String CONTENT_ITEM_TYPE =
      ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + FeedContract.AUTHORITY + "/" + PATH_PREVIEW;
  private static final String PATH_PREVIEW_ID = "/preview/";
  public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + FeedContract.AUTHORITY + PATH_PREVIEW_ID);

  public static long getIdFromUri(Uri uri) {

    return Long.parseLong(uri.getPathSegments().get(1));
  }

  public static Uri buildPreviewUriById(long id) {
    return ContentUris.withAppendedId(CONTENT_URI, id);
  }

  public static Uri buildSetLikeInPreviewUriById(long id) {
    return ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
  }

  public static Uri buildSetAuthorImageInPreviewUriById(long id) {
    return ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
  }

  public static Uri buildSetDescriptionInPreviewById(long id) {
    return ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
  }

  public static Uri buildSetTitleInPreviewById(long id) {
    return ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
  }

  public static Uri buildSetImageUrlInPreviewById(long id) {
    return ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
  }

  public static Uri buildPreviewUriByAuthor(String author) {
    return CONTENT_URI.buildUpon().appendPath(author).build();
  }


  public static Uri buildIsMyPreview(String isMyFlag) {
    return CONTENT_URI.buildUpon().appendPath(isMyFlag).build();
  }

  public static Uri buildGetPreviewById(long id) {
    return ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
  }


  public static String getAuthorFromUri(Uri uri) {
    return uri.getPathSegments().get(1);
  }
}
