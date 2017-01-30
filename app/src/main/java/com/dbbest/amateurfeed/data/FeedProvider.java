package com.dbbest.amateurfeed.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by antonina on 27.01.17.
 */

public class FeedProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FeedDbHelper mOpenHelper;

    static final int PREVIEW = 100;
    static final int COMMENT = 300;
    static final int PROFILE = 400;
    static final int CREATOR = 500;
    static final int TAG = 600;
    static final int PREVIEW_TAG = 700;
    static final int PREVIEW_TAG_BY_ID = 101;
    static final int USER_NEWS = 800;

    private static final SQLiteQueryBuilder sCommentByCreatorQueryBuilder;


    static {
        sCommentByCreatorQueryBuilder = new SQLiteQueryBuilder();


        //This is an inner join which looks like
        //comment INNER JOIN creator ON comment.creator_id = creator._id
        sCommentByCreatorQueryBuilder.setTables(
                FeedContract.CommentEntry.TABLE_NAME + " INNER JOIN " +
                        FeedContract.CreatorEntry.TABLE_NAME +
                        " ON " + FeedContract.CommentEntry.TABLE_NAME +
                        "." + FeedContract.CommentEntry.COLUMN_CREATOR_KEY +
                        " = " + FeedContract.CreatorEntry.TABLE_NAME +
                        "." + FeedContract.CreatorEntry._ID);
    }


    private static SQLiteQueryBuilder sPreviewByTagQueryBuilder;

    static {

        sPreviewByTagQueryBuilder = new SQLiteQueryBuilder();

        //preview JOIN tag on table preview_tag
        sPreviewByTagQueryBuilder.setTables(FeedContract.PreviewEntry.TABLE_NAME +
                " LEFT OUTER JOIN " + FeedContract.PreviewTagEntry.TABLE_NAME +
                " ON " + FeedContract.PreviewEntry.TABLE_NAME + "." + FeedContract.PreviewEntry._ID + " = " + FeedContract.PreviewTagEntry.TABLE_NAME + "." + FeedContract.PreviewTagEntry.COLUMN_PREVIEW_ID +
                " LEFT JOIN " + FeedContract.TagEntry.TABLE_NAME +
                " ON " + FeedContract.TagEntry.TABLE_NAME +"."+ FeedContract.TagEntry._ID + " = " + FeedContract.PreviewTagEntry.TABLE_NAME + "." + FeedContract.PreviewTagEntry.COLUMN_TAG_ID);
    }


    //creator._id = ?
    private static final String sCreatorSelection =
            FeedContract.CreatorEntry.TABLE_NAME +

                    "." + FeedContract.CreatorEntry._ID + " = ? ";
    //preview._id = ?
    private static final String sPreviewSelection =
            FeedContract.PreviewEntry.TABLE_NAME +
                    "." + FeedContract.PreviewEntry._ID + " = ? ";

    //comment._id=?
    private static final String sCommentSelection =
            FeedContract.CommentEntry.TABLE_NAME +
                    "." + FeedContract.CommentEntry._ID + " = ? ";

    private Cursor getPreviewByIdSelection(
            Uri uri, String[] projection, String sortOrder) {
        long id = FeedContract.PreviewEntry.getIdFromUri(uri);

        Cursor query = sPreviewByTagQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sPreviewSelection,
                new String[]{Long.toString(id)},
                null,
                null,
                sortOrder
        );
        Log.i("TestProvider", "projection: "+  projection );
        Log.i("TestProvider", sPreviewByTagQueryBuilder.getTables() );
        Log.i("TestProvider", "selection: "+  sPreviewSelection );
        Log.i("TestProvider", "value  of selection: "+  id );

        return query;
    }


    static UriMatcher buildUriMatcher() {


        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FeedContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, FeedContract.PATH_PREVIEW, PREVIEW);
        matcher.addURI(authority, FeedContract.PATH_COMMENT, COMMENT);
        matcher.addURI(authority, FeedContract.PATH_CREATOR, CREATOR);
        matcher.addURI(authority, FeedContract.PATH_TAG, TAG);
        matcher.addURI(authority, FeedContract.PATH_PROFILE, PROFILE);
        matcher.addURI(authority, FeedContract.PATH_PREVIEW_TAG, PREVIEW_TAG);
        matcher.addURI(authority, FeedContract.PATH_USER_NEWS, USER_NEWS);
        matcher.addURI(authority, FeedContract.PATH_PREVIEW+ "/#", PREVIEW_TAG_BY_ID);

//        matcher.addURI(authority, FeedContract.PATH_PREVIEW + "/*/#", PREVIEW_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new FeedDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PREVIEW:
                return FeedContract.PreviewEntry.CONTENT_TYPE;
            case PREVIEW_TAG_BY_ID:
                return FeedContract.PreviewEntry.CONTENT_ITEM_TYPE;
            case USER_NEWS:
                return FeedContract.UserNewsEntry.CONTENT_TYPE;
            case COMMENT:
                return FeedContract.CommentEntry.CONTENT_TYPE;
            case PROFILE:
                return FeedContract.UserProfileEntry.CONTENT_TYPE;
            case CREATOR:
                return FeedContract.CreatorEntry.CONTENT_TYPE;
            case TAG:
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
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {


            case PREVIEW: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FeedContract.PreviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        sortOrder,
                        null
                );
                break;
            }

            case PREVIEW_TAG_BY_ID: {
                retCursor = getPreviewByIdSelection(uri, projection, sortOrder);
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
                if (_id > 0)
                    returnUri = FeedContract.PreviewEntry.buildPreviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case COMMENT: {
                long _id = db.insert(FeedContract.CommentEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FeedContract.CommentEntry.buildCommentUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PROFILE: {
                long _id = db.insert(FeedContract.UserProfileEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FeedContract.UserProfileEntry.buildUserProfileUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CREATOR: {
                long _id = db.insert(FeedContract.CreatorEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FeedContract.CreatorEntry.buildCreatorUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case USER_NEWS: {
                long _id = db.insert(FeedContract.UserNewsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FeedContract.UserNewsEntry.buildUserNewsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TAG: {
                long _id = db.insert(FeedContract.TagEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FeedContract.TagEntry.buildTagUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PREVIEW_TAG: {
                long _id = db.insert(FeedContract.PreviewTagEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FeedContract.PreviewTagEntry.buildPreviewTagUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
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
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
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
        // Because a null deletes all rows
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
            case COMMENT:
                rowsUpdated = db.update(FeedContract.CommentEntry.TABLE_NAME, values, selection, selectionArgs);
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