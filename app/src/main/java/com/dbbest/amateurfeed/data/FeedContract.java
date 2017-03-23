package com.dbbest.amateurfeed.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class FeedContract {

  public static final String AUTHORITY = "com.dbbest.amateurfeed.app";
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
  public static final String PATH_PREVIEW = "preview";
  public static final String PATH_COMMENT = "comment";
  public static final String PATH_TAG = "tag";
  public static final String PATH_PREVIEW_TAG = "preview_tag";
  public static final String PATH_CREATOR = "creator";
  public static final String PATH_USER_NEWS = "user_news";
  public static final String PATH_PROFILE = "profile";

  public static final class UserNewsEntry implements BaseColumns {

    public static final Uri CONTENT_URI =
        BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_NEWS).build();
    public static final String CONTENT_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_USER_NEWS;
    public static final String CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_USER_NEWS;
    public static final String TABLE_NAME = "user_news";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_UPDATE_DATE = "update_date";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_LIKES = "likes";
    public static final int PREVIEW_ID_PATH_POSITION = 1;
    private static final String PATH_USER_NEWS_ID = "/" + PATH_USER_NEWS + "/";
    public static final Uri CONTENT_ID_URI_BASE = Uri.parse(BASE_CONTENT_URI + PATH_USER_NEWS_ID);

    public static final String[] NEWS_COLUMNS = {
        FeedContract.UserNewsEntry.TABLE_NAME + "." + FeedContract.UserNewsEntry._ID,
        FeedContract.UserNewsEntry.COLUMN_TITLE,
        UserNewsEntry.COLUMN_UPDATE_DATE,
        UserNewsEntry.COLUMN_STATUS,
        UserNewsEntry.COLUMN_IMAGE,
        UserNewsEntry.COLUMN_LIKES,
    };



    public static Uri buildUserNewsUri(long id) {
      return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static Uri buildGetNewsById(long id) {
      return ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
    }
  }

  public static final class UserProfileEntry implements BaseColumns {

    public static final Uri CONTENT_URI =
        BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROFILE).build();
    public static final String CONTENT_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_PROFILE;
    public static final String CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_PROFILE;

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

  public static final class CommentEntry implements BaseColumns {

    public static final int COMMENT_ID_PATH_POSITION = 1;
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMENT)
        .build();
    public static final String CONTENT_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_COMMENT;
    public static final String CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_COMMENT;

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

  public static final class TagEntry implements BaseColumns {

    public static final int TAG_ID_PATH_POSITION = 1;
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TAG).build();
    public static final String CONTENT_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_TAG;
    public static final String CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_PROFILE;

    public static final String TABLE_NAME = "tag";
    public static final String COLUMN_TAG_ID = "tag_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PREVIEW_ID = "preview_id";
    private static final String SCHEME = "content://";
    private static final String PATH_TAG_ID = "/tag/";
    public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_TAG_ID);

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

  public static final class PreviewEntry implements BaseColumns {

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
        FeedContract.PreviewEntry.TABLE_NAME + "." + FeedContract.PreviewEntry._ID,
        PreviewEntry.COLUMN_TITLE,
        PreviewEntry.COLUMN_TEXT,
        PreviewEntry.COLUMN_LIKES,
        PreviewEntry.COLUMN_IS_LIKE,
        PreviewEntry.COLUMN_AUTHOR,
        PreviewEntry.COLUMN_AUTHOR_IMAGE,
        PreviewEntry.COLUMN_CREATE_DATE,
        PreviewEntry.COLUMN_IMAGE,
        PreviewEntry.COLUMN_IS_MY

    };
    private static final String SCHEME = "content://";
    private static final String PATH_PREVIEW = "/preview";
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_PREVIEW);
    public static final String CONTENT_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_PREVIEW;
    public static final String CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_PREVIEW;
    private static final String PATH_PREVIEW_ID = "/preview/";
    public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_PREVIEW_ID);

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

  public static final class CreatorEntry implements BaseColumns {

    public static final int CREATOR_ID_PATH_POSITION = 1;
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CREATOR)
        .build();
    public static final String CONTENT_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_CREATOR;
    public static final String CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_CREATOR;

    public static final String TABLE_NAME = "creator";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IS_ADMIN = "is_admin";
    public static final String COLUMN_IMAGE = "image";
    private static final String SCHEME = "content://";
    private static final String PATH_CREATOR_ID = "/creator/";
    public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_CREATOR_ID);

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

  public static final class PreviewTagEntry implements BaseColumns {

    public static final Uri CONTENT_URI =
        BASE_CONTENT_URI.buildUpon().appendPath(PATH_PREVIEW_TAG).build();
    public static final String CONTENT_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_PREVIEW_TAG;
    public static final String CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_PREVIEW_TAG;

    public static final String TABLE_NAME = "preview_tag";
    public static final String COLUMN_PREVIEW_ID = "preview_id";
    public static final String COLUMN_TAG_ID = "tag_id";
    private static final String SCHEME = "content://";
    private static final String PATH_PREVIEW_TAG_ID = "/preview_tag/";
    public static final Uri CONTENT_ID_URI_BASE = Uri
        .parse(SCHEME + AUTHORITY + PATH_PREVIEW_TAG_ID);

    public static Uri buildTagUriById(long id) {
      return ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
    }
  }

}
