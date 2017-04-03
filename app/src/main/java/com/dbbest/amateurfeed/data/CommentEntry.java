package com.dbbest.amateurfeed.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class CommentEntry implements BaseColumns {

  public static final int COMMENT_ID_PATH_POSITION = 1;
  public static final Uri CONTENT_URI = FeedContract.BASE_CONTENT_URI.buildUpon().appendPath(FeedContract.PATH_COMMENT).build();
  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + FeedContract.AUTHORITY + "/" + FeedContract.PATH_COMMENT;
  public static final String CONTENT_ITEM_TYPE =
      ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + FeedContract.AUTHORITY + "/" + FeedContract.PATH_COMMENT;
  public static final String TABLE_NAME = "comment";
  public static final String COLUMN_CREATOR_KEY = "creator_id";
  public static final String COLUMN_POST_ID = "post_id";
  public static final String COLUMN_BODY = "body";
  public static final String COLUMN_PARENT_COMMENT_ID = "parent_comment_id";
  public static final String COLUMN_CREATE_DATE = "create_date";

  public static Uri buildCommentUri(long id) {
    return ContentUris.withAppendedId(CONTENT_URI, id);
  }

  public static Uri getCommentsListById(long id) {
    return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
  }
}
