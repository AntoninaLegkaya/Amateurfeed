package com.dbbest.amateurfeed.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import com.dbbest.amateurfeed.data.FeedContract.CreatorEntry;
import com.dbbest.amateurfeed.data.FeedContract.UserNewsEntry;
import java.util.HashMap;

public class FeedProvider extends ContentProvider {

  public static final String sPreviewSelection =
      FeedContract.PreviewEntry.TABLE_NAME +
          "." + FeedContract.PreviewEntry._ID + " = ? ";
  public static final String sPreviewSelectionId =
      FeedContract.PreviewEntry.TABLE_NAME +
          "." + FeedContract.PreviewEntry._ID;
  public static final String sTagSelection =
      FeedContract.TagEntry.TABLE_NAME +
          "." + FeedContract.TagEntry._ID + " = ? ";
  public static final String sCommentSelection =
      FeedContract.CommentEntry.TABLE_NAME +
          "." + FeedContract.CommentEntry._ID + " = ? ";
  public static final String sCreatorSelection =
      FeedContract.CreatorEntry.TABLE_NAME +
          "." + FeedContract.CreatorEntry._ID + " = ? ";
  static final int PREVIEW = 100;
  static final int PREVIEW_ID = 101;
  static final int PREVIEW_AUTHOR = 102;
  static final int COMMENT = 300;
  static final int COMMENT_POST_ID = 301;
  static final int PROFILE = 400;
  static final int CREATOR = 500;
  static final int CREATOR_ID = 501;
  static final int CREATOR_AUTHOR = 502;
  static final int TAG = 600;
  static final int TAG_ID = 601;
  static final int TAG_PREVIEW_ID = 602;
  static final int PREVIEW_TAG = 700;
  static final int PREVIEW_TAG_ID = 701;
  static final int USER_NEWS = 800;
  static final int USER_NEWS_ID = 801;
  private static final UriMatcher sUriMatcher = buildUriMatcher();
  private static final SQLiteQueryBuilder sCommentByCreatorQueryBuilder;
  private static final SQLiteQueryBuilder sPreviewByTagQueryBuilder;
  private static final SQLiteQueryBuilder sPreviewByIdQueryBuilder;
  private static final SQLiteQueryBuilder sPreviewByAuthorQueryBuilder;
  private static final SQLiteQueryBuilder sTagByIdQueryBuilder;
  private static final SQLiteQueryBuilder sCreatorByIdQueryBuilder;
  private static final SQLiteQueryBuilder sCreatorByAuthorQueryBuilder;
  private static final SQLiteQueryBuilder sCommentByIdQueryBuilder;
  private static final String sCreatorSelectionByAuthorName =
      FeedContract.CreatorEntry.TABLE_NAME +
          "." + CreatorEntry.COLUMN_NAME + " = ? ";
  private static final String sTagsListSelection =
      FeedContract.TagEntry.COLUMN_PREVIEW_ID + " = ? ";
  private static final String sCommentListSelection =
      FeedContract.CommentEntry.COLUMN_POST_ID + " = ? ";
  private static final String sPreviewSelectionAuthor =
      FeedContract.PreviewEntry.TABLE_NAME +
          "." + FeedContract.PreviewEntry.COLUMN_AUTHOR + " = ? ";
  public static long TEST_TAG_ID = 1;
  public static String TEST_TAG_AUTHOR = "Tony";
  public static long TEST_ID = 1;
  private static HashMap<String, String> sUserNesProjectionMap = new HashMap<String, String>();
  private static HashMap<String, String> sPreviewProjectionMap = new HashMap<String, String>();

  static {
    for (int i = 0; i < UserNewsEntry.NEWS_COLUMNS.length; i++) {
      sUserNesProjectionMap.put(
          UserNewsEntry.NEWS_COLUMNS[i],
          UserNewsEntry.NEWS_COLUMNS[i]);
    }
  }

  static {
    for (int i = 0; i < FeedContract.PreviewEntry.DEFAULT_PROJECTION.length; i++) {
      sPreviewProjectionMap.put(
          FeedContract.PreviewEntry.DEFAULT_PROJECTION[i],
          FeedContract.PreviewEntry.DEFAULT_PROJECTION[i]);
    }
  }

  static {
    sCommentByCreatorQueryBuilder = new SQLiteQueryBuilder();
    sCommentByCreatorQueryBuilder.setTables(
        FeedContract.CommentEntry.TABLE_NAME + " INNER JOIN " +
            FeedContract.CreatorEntry.TABLE_NAME +
            " ON " + FeedContract.CommentEntry.TABLE_NAME +
            "." + FeedContract.CommentEntry.COLUMN_CREATOR_KEY +
            " = " + FeedContract.CreatorEntry.TABLE_NAME +
            "." + FeedContract.CreatorEntry._ID);
  }

  static {
    sPreviewByTagQueryBuilder = new SQLiteQueryBuilder();
    sPreviewByTagQueryBuilder.setTables(FeedContract.PreviewEntry.TABLE_NAME +
        " LEFT OUTER JOIN " + FeedContract.PreviewTagEntry.TABLE_NAME +
        " ON " + FeedContract.PreviewEntry.TABLE_NAME + "." + FeedContract.PreviewEntry._ID + " = "
        + FeedContract.PreviewTagEntry.TABLE_NAME + "."
        + FeedContract.PreviewTagEntry.COLUMN_PREVIEW_ID +
        " LEFT JOIN " + FeedContract.TagEntry.TABLE_NAME +
        " ON " + "(" + FeedContract.TagEntry.TABLE_NAME + "." + FeedContract.TagEntry._ID + " = "
        + FeedContract.PreviewTagEntry.TABLE_NAME + "." + FeedContract.PreviewTagEntry.COLUMN_TAG_ID
        + ")");
  }

  static {
    sPreviewByIdQueryBuilder = new SQLiteQueryBuilder();
    sPreviewByIdQueryBuilder.setTables(FeedContract.PreviewEntry.TABLE_NAME);
  }

  static {
    sPreviewByAuthorQueryBuilder = new SQLiteQueryBuilder();
    sPreviewByAuthorQueryBuilder.setTables(FeedContract.PreviewEntry.TABLE_NAME);
  }

  static {
    sTagByIdQueryBuilder = new SQLiteQueryBuilder();
    sTagByIdQueryBuilder.setTables(FeedContract.TagEntry.TABLE_NAME);
  }

  static {
    sCreatorByIdQueryBuilder = new SQLiteQueryBuilder();
    sCreatorByIdQueryBuilder.setTables(FeedContract.CreatorEntry.TABLE_NAME);
  }

  static {
    sCreatorByAuthorQueryBuilder = new SQLiteQueryBuilder();
    sCreatorByAuthorQueryBuilder.setTables(FeedContract.CreatorEntry.TABLE_NAME);
  }

  static {
    sCommentByIdQueryBuilder = new SQLiteQueryBuilder();
    sCommentByIdQueryBuilder.setTables(FeedContract.CommentEntry.TABLE_NAME);
  }

  private final String TAG_LOG = FeedProvider.class.getName();
  private FeedDbHelper mOpenHelper;

  static UriMatcher buildUriMatcher() {

    final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    final String authority = FeedContract.AUTHORITY;
    matcher.addURI(authority, FeedContract.PATH_PREVIEW, PREVIEW);
    matcher.addURI(authority, FeedContract.PATH_PREVIEW + "/#", PREVIEW_ID);
    matcher.addURI(authority, FeedContract.PATH_CREATOR + "/#", CREATOR_ID);
    matcher.addURI(authority, FeedContract.PATH_PREVIEW + "/*", PREVIEW_AUTHOR);
    matcher.addURI(authority, FeedContract.PATH_COMMENT, COMMENT);
    matcher.addURI(authority, FeedContract.PATH_COMMENT + "/#", COMMENT_POST_ID);
    matcher.addURI(authority, FeedContract.PATH_CREATOR, CREATOR);
    matcher.addURI(authority, FeedContract.PATH_CREATOR + "/*", CREATOR_AUTHOR);
    matcher.addURI(authority, FeedContract.PATH_TAG, TAG);
    matcher.addURI(authority, FeedContract.PATH_TAG + "/#", TAG_ID);
    matcher.addURI(authority, FeedContract.PATH_TAG + "/#", TAG_PREVIEW_ID);
    matcher.addURI(authority, FeedContract.PATH_PROFILE, PROFILE);
    matcher.addURI(authority, FeedContract.PATH_PREVIEW_TAG, PREVIEW_TAG);
    matcher.addURI(authority, FeedContract.PATH_USER_NEWS, USER_NEWS);
    matcher.addURI(authority, FeedContract.PATH_USER_NEWS + "/#", USER_NEWS_ID);
    matcher.addURI(authority, FeedContract.PATH_PREVIEW_TAG + "/#", PREVIEW_TAG_ID);
    return matcher;
  }

  private Cursor getPreviewByIdSelection(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(FeedContract.PreviewEntry.TABLE_NAME);
    qb.setProjectionMap(sPreviewProjectionMap);
    qb.appendWhere(FeedContract.PreviewEntry._ID + "=" + uri.getPathSegments()
        .get(FeedContract.PreviewEntry.PREVIEW_ID_PATH_POSITION));
    String orderBy = null;
    SQLiteDatabase db = mOpenHelper.getReadableDatabase();

    return qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
  }

  private Cursor getUserNewsByIdSelection(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(FeedContract.UserNewsEntry.TABLE_NAME);
    qb.setProjectionMap(sUserNesProjectionMap);
    qb.appendWhere(FeedContract.UserNewsEntry._ID + "=" + uri.getPathSegments()
        .get(FeedContract.UserNewsEntry.PREVIEW_ID_PATH_POSITION));
    String orderBy = null;
    SQLiteDatabase db = mOpenHelper.getReadableDatabase();

    return qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
  }

  private Cursor getPreviewByAuthor(Uri uri, String[] projection, String sortOrder) {

    String author = FeedContract.PreviewEntry.getAuthorFromUri(uri);
    String[] selectionArgs;
    String selection;
    selectionArgs = new String[]{author};
    selection = sPreviewSelectionAuthor;

    return sPreviewByAuthorQueryBuilder.query(mOpenHelper.getReadableDatabase(),
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    );

  }

  private Cursor getPreviewByIdPreviewTagSelection(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    sPreviewByTagQueryBuilder.appendWhere(FeedContract.PreviewTagEntry.TABLE_NAME + "."
        + FeedContract.PreviewTagEntry.COLUMN_PREVIEW_ID + "=" + uri.getPathSegments()
        .get(FeedContract.PreviewEntry.PREVIEW_ID_PATH_POSITION));

    Cursor c = sPreviewByTagQueryBuilder.query(mOpenHelper.getReadableDatabase(),
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    );
    if (c.moveToFirst()) {
      Log.i(TAG_LOG, DatabaseUtils.dumpCursorToString(c));
    }
    return c;
  }

  private Cursor getTagById(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    sTagByIdQueryBuilder.appendWhere(FeedContract.TagEntry._ID + "=" + uri.getPathSegments()
        .get(FeedContract.TagEntry.TAG_ID_PATH_POSITION));
    Cursor c = sTagByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    );
    return c;
  }

  private Cursor getTagsListByIdPreview(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    String id = uri.getPathSegments().get(FeedContract.TagEntry.TAG_ID_PATH_POSITION);

    Cursor c = sTagByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
        projection,
        sTagsListSelection,
        new String[]{id},
        null,
        null,
        sortOrder
    );
    return c;
  }

  private Cursor getCommentsListByPostId(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    String id = uri.getPathSegments().get(FeedContract.CommentEntry.COMMENT_ID_PATH_POSITION);

    Cursor c = sCommentByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
        projection,
        sCommentListSelection,
        new String[]{id},
        null,
        null,
        sortOrder
    );
    return c;
  }

  private Cursor getCreatorById(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    String id = uri.getPathSegments().get(FeedContract.CreatorEntry.CREATOR_ID_PATH_POSITION);

    Cursor c = sCreatorByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
        projection,
        sCreatorSelection,
        new String[]{id},
        null,
        null,
        sortOrder
    );
    return c;
  }

  private Cursor getIdCreatorByAuthor(Uri uri, String[] projection, String sortOrder) {

    String author = FeedContract.CreatorEntry.getAuthorFromUri(uri);
    String[] selectionArgs;
    String selection;
    selectionArgs = new String[]{author};
    selection = sCreatorSelectionByAuthorName;

    return sCreatorByAuthorQueryBuilder.query(mOpenHelper.getReadableDatabase(),
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    );
  }

  private int updateColumnInPreview(Uri uri, ContentValues cv, String selection,
      String[] selectionArgs) {
    String id = uri.getPathSegments().get(FeedContract.PreviewEntry.PREVIEW_ID_PATH_POSITION);
    int rowsUpdated = mOpenHelper.getWritableDatabase().update(
        FeedContract.PreviewEntry.TABLE_NAME, cv, sPreviewSelection, new String[]{id});
    return rowsUpdated;
  }

  @Override
  public boolean onCreate() {
    getContext().deleteDatabase(FeedDbHelper.DATABASE_NAME);
    mOpenHelper = new FeedDbHelper(getContext());
    return true;
  }

  @Override
  public String getType(Uri uri) {
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case PREVIEW:
        return FeedContract.PreviewEntry.CONTENT_TYPE;
      case PREVIEW_ID:
        return FeedContract.PreviewEntry.CONTENT_ITEM_TYPE;
      case PREVIEW_AUTHOR:
        return FeedContract.PreviewEntry.CONTENT_TYPE;
      case PREVIEW_TAG_ID:
        return FeedContract.PreviewTagEntry.CONTENT_ITEM_TYPE;
      case USER_NEWS:
        return FeedContract.UserNewsEntry.CONTENT_TYPE;
      case USER_NEWS_ID:
        return FeedContract.UserNewsEntry.CONTENT_ITEM_TYPE;
      case COMMENT:
        return FeedContract.CommentEntry.CONTENT_TYPE;
      case COMMENT_POST_ID:
        return FeedContract.CommentEntry.CONTENT_TYPE;
      case PROFILE:
        return FeedContract.UserProfileEntry.CONTENT_TYPE;
      case CREATOR:
        return FeedContract.CreatorEntry.CONTENT_TYPE;
      case CREATOR_ID:
        return FeedContract.CreatorEntry.CONTENT_ITEM_TYPE;
      case CREATOR_AUTHOR:
        return FeedContract.CreatorEntry.CONTENT_ITEM_TYPE;
      case TAG:
        return FeedContract.TagEntry.CONTENT_TYPE;
      case TAG_ID:
        return FeedContract.TagEntry.CONTENT_ITEM_TYPE;
      case TAG_PREVIEW_ID:
        return FeedContract.TagEntry.CONTENT_TYPE;
      case PREVIEW_TAG:
        return FeedContract.PreviewTagEntry.CONTENT_TYPE;

      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    Cursor retCursor;
    switch (sUriMatcher.match(uri)) {
      case PREVIEW: {
        retCursor = mOpenHelper.getReadableDatabase().query(
            FeedContract.PreviewEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder

        );
        break;
      }
      case PREVIEW_ID: {
        retCursor = getPreviewByIdSelection(uri, projection, selection, selectionArgs, sortOrder);
        break;
      }
      case PREVIEW_AUTHOR: {
        retCursor = getPreviewByAuthor(uri, projection, sortOrder);
        break;
      }

      case COMMENT: {
        retCursor = mOpenHelper.getReadableDatabase().query(
            FeedContract.CommentEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        );
        break;
      }

      case COMMENT_POST_ID: {
        retCursor = getCommentsListByPostId(uri, projection, selection, selectionArgs, sortOrder);
        break;
      }
      case PROFILE: {
        retCursor = mOpenHelper.getReadableDatabase().query(
            FeedContract.PreviewEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        );
        break;
      }

      case USER_NEWS: {
        retCursor = mOpenHelper.getReadableDatabase().query(
            FeedContract.UserNewsEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        );
        break;
      }

      case USER_NEWS_ID: {
        retCursor = getUserNewsByIdSelection(uri, projection, selection, selectionArgs, sortOrder);
        break;
      }
      case CREATOR: {
        retCursor = mOpenHelper.getReadableDatabase().query(
            FeedContract.CreatorEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        );
        break;
      }
      case CREATOR_ID: {
        retCursor = getCreatorById(uri, projection, selection, selectionArgs, sortOrder);
        break;
      }

      case CREATOR_AUTHOR: {
        retCursor = getIdCreatorByAuthor(uri, projection, sortOrder);
        break;
      }
      case TAG: {
        retCursor = mOpenHelper.getReadableDatabase().query(
            FeedContract.TagEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        );
        break;
      }
      case TAG_ID: {
        retCursor = getTagById(uri, projection, selection, selectionArgs, sortOrder);
        break;
      }
      case TAG_PREVIEW_ID: {
        retCursor = getTagsListByIdPreview(uri, projection, selection, selectionArgs, sortOrder);
        break;
      }
      case PREVIEW_TAG: {
        retCursor = mOpenHelper.getReadableDatabase().query(
            FeedContract.PreviewTagEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        );
        break;
      }
      case PREVIEW_TAG_ID: {
        retCursor = getPreviewByIdPreviewTagSelection(uri, projection, selection, selectionArgs,
            sortOrder);
        break;
      }

      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    retCursor.setNotificationUri(getContext().getContentResolver(), uri);
    return retCursor;
  }


  @Override
  public Uri insert(Uri uri, ContentValues values) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    Uri returnUri;

    switch (match) {
      case PREVIEW: {
        long _id = db.insert(FeedContract.PreviewEntry.TABLE_NAME, null, values);
        if (_id > 0) {
          TEST_ID = _id;
          returnUri = ContentUris
              .withAppendedId(FeedContract.PreviewEntry.CONTENT_ID_URI_BASE, _id);
        } else {
          throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        break;
      }
      case COMMENT: {
        long _id = db.insert(FeedContract.CommentEntry.TABLE_NAME, null, values);
        if (_id > 0) {
          returnUri = FeedContract.CommentEntry.buildCommentUri(_id);
        } else {
          throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        break;
      }
      case PROFILE: {
        long _id = db.insert(FeedContract.UserProfileEntry.TABLE_NAME, null, values);
        if (_id > 0) {
          returnUri = FeedContract.UserProfileEntry.buildUserProfileUri(_id);
        } else {
          throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        break;
      }
      case CREATOR: {
        long _id = db.insert(FeedContract.CreatorEntry.TABLE_NAME, null, values);
        if (_id > 0) {
          returnUri = FeedContract.CreatorEntry.buildCreatorUri(_id);
        } else {
          throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        break;
      }
      case USER_NEWS: {
        long _id = db.insert(FeedContract.UserNewsEntry.TABLE_NAME, null, values);
        if (_id > 0) {
          returnUri = FeedContract.UserNewsEntry.buildUserNewsUri(_id);
        } else {
          throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        break;
      }
      case TAG: {
        long _id = db.insert(FeedContract.TagEntry.TABLE_NAME, null, values);
        if (_id > 0) {
          TEST_TAG_ID = _id;
          returnUri = ContentUris
              .withAppendedId(FeedContract.TagEntry.CONTENT_ID_URI_BASE, _id);
        } else {
          throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        break;
      }
      case PREVIEW_TAG: {
        long _id = db.insert(FeedContract.PreviewTagEntry.TABLE_NAME, null, values);
        if (_id > 0) {
          returnUri = FeedContract.PreviewTagEntry.buildTagUriById(_id);
        } else {
          throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        break;
      }
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return returnUri;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    int rowsDeleted;
    if (null == selection) {
      selection = "1";
    }
    switch (match) {
      case PREVIEW:
        rowsDeleted = db.delete(
            FeedContract.PreviewEntry.TABLE_NAME, selection, selectionArgs);
        break;
      case COMMENT:
        rowsDeleted = db.delete(
            FeedContract.CommentEntry.TABLE_NAME, selection, selectionArgs);
        break;
      case PROFILE:
        rowsDeleted = db.delete(
            FeedContract.UserProfileEntry.TABLE_NAME, selection, selectionArgs);
        break;
      case CREATOR:
        rowsDeleted = db.delete(
            FeedContract.CreatorEntry.TABLE_NAME, selection, selectionArgs);
        break;
      case TAG:
        rowsDeleted = db.delete(
            FeedContract.TagEntry.TABLE_NAME, selection, selectionArgs);
        break;
      case USER_NEWS:
        rowsDeleted = db.delete(
            FeedContract.UserNewsEntry.TABLE_NAME, selection, selectionArgs);
        break;
      case PREVIEW_TAG:
        rowsDeleted = db.delete(
            FeedContract.PreviewTagEntry.TABLE_NAME, selection, selectionArgs);
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    if (rowsDeleted != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsDeleted;
  }

  @Override
  public int update(
      Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    int rowsUpdated;

    switch (match) {
      case PREVIEW:
        rowsUpdated = db.update(
            FeedContract.PreviewEntry.TABLE_NAME, values, selection, selectionArgs);
        break;
      case PREVIEW_ID:
        rowsUpdated = updateColumnInPreview(uri, values, selection, selectionArgs);
        break;
      case COMMENT:
        rowsUpdated = db
            .update(FeedContract.CommentEntry.TABLE_NAME, values, selection, selectionArgs);
        break;
      case PROFILE:
        rowsUpdated = db.update(
            FeedContract.UserProfileEntry.TABLE_NAME, values, selection, selectionArgs);
        break;
      case CREATOR:
        rowsUpdated = db.update(
            FeedContract.CreatorEntry.TABLE_NAME, values, selection, selectionArgs);
        break;
      case TAG:
        rowsUpdated = db.update(
            FeedContract.TagEntry.TABLE_NAME, values, selection, selectionArgs);
        break;
      case USER_NEWS:
        rowsUpdated = db.update(
            FeedContract.UserNewsEntry.TABLE_NAME, values, selection, selectionArgs);
        break;
      case PREVIEW_TAG:
        rowsUpdated = db.update(
            FeedContract.PreviewTagEntry.TABLE_NAME, values, selection, selectionArgs);
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    if (rowsUpdated != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsUpdated;
  }

  @Override
  public int bulkInsert(Uri uri, ContentValues[] values) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case PREVIEW: {
        db.beginTransaction();
        int returnCount = 0;
        try {
          for (ContentValues value : values) {
            long _id = db.insert(FeedContract.PreviewEntry.TABLE_NAME, null, value);
            if (_id != -1) {
              returnCount++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
      }
      case COMMENT: {
        db.beginTransaction();
        int returnCount = 0;
        try {
          for (ContentValues value : values) {
            long _id = db.insert(FeedContract.CommentEntry.TABLE_NAME, null, value);
            if (_id != -1) {
              returnCount++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
      }
      case USER_NEWS: {
        db.beginTransaction();
        int returnCount = 0;
        try {
          for (ContentValues value : values) {
            long _id = db.insert(FeedContract.UserNewsEntry.TABLE_NAME, null, value);
            if (_id != -1) {
              returnCount++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
      }
      case TAG: {
        db.beginTransaction();
        int returnCount = 0;
        try {
          for (ContentValues value : values) {
            long _id = db.insert(FeedContract.TagEntry.TABLE_NAME, null, value);
            if (_id != -1) {
              returnCount++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
      }
      default:
        return super.bulkInsert(uri, values);
    }
  }

  @Override
  @TargetApi(11)
  public void shutdown() {
    mOpenHelper.close();
    super.shutdown();
  }
}