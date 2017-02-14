package com.dbbest.amateurfeed.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by antonina on 27.01.17.
 */

public class FeedDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "feed.db";

    public FeedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_USER_NEWS_TABLE = "CREATE TABLE " + FeedContract.UserNewsEntry.TABLE_NAME + " (" +
                FeedContract.UserNewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FeedContract.UserNewsEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                FeedContract.UserNewsEntry.COLUMN_UPDATE_DATE + " TEXT NOT NULL, " +
                FeedContract.UserNewsEntry.COLUMN_STATUS + " TEXT NOT NULL, " +
                FeedContract.UserNewsEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                FeedContract.UserNewsEntry.COLUMN_LIKES + " INTEGER " +
                " );";


        final String SQL_CREATE_PROFILE_USER_TABLE = "CREATE TABLE " + FeedContract.UserProfileEntry.TABLE_NAME + " (" +
                FeedContract.UserProfileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FeedContract.UserProfileEntry.COLUMN_FULL_NAME + " TEXT UNIQUE NOT NULL, " +
                FeedContract.UserProfileEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                FeedContract.UserProfileEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                FeedContract.UserProfileEntry.COLUMN_SKYPE + " TEXT NOT NULL, " +
                FeedContract.UserProfileEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                FeedContract.UserProfileEntry.COLUMN_JOB + " TEXT NOT NULL, " +
                FeedContract.UserProfileEntry.COLUMN_PHONE + " TEXT NOT NULL" +
                " );";


        final String SQL_CREATE_COMMENT_USER_TABLE = "CREATE TABLE " + FeedContract.CommentEntry.TABLE_NAME + " (" +
                FeedContract.CommentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                FeedContract.CommentEntry.COLUMN_POST_ID + " INTEGER NOT NULL, " +
                FeedContract.CommentEntry.COLUMN_CREATOR_KEY + " INTEGER NOT NULL, " +
                FeedContract.CommentEntry.COLUMN_BODY + " TEXT  NOT NULL, " +
                FeedContract.CommentEntry.COLUMN_PARENT_COMMENT_ID + " INTEGER NOT NULL, " +
                FeedContract.CommentEntry.COLUMN_CREATE_DATE + " TEXT  NOT NULL, " +
                // Set up the creator column as a foreign key to creator table.
                " FOREIGN KEY (" + FeedContract.CommentEntry.COLUMN_CREATOR_KEY + ") REFERENCES " +
                FeedContract.CreatorEntry.TABLE_NAME + " (" + FeedContract.CreatorEntry._ID + ")" +

                ");";


        final String SQL_CREATE_TAG_TABLE = "CREATE TABLE " + FeedContract.TagEntry.TABLE_NAME + " (" +
                FeedContract.TagEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FeedContract.TagEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                FeedContract.TagEntry.COLUMN_TAG_ID + " INTEGER NOT NULL, " +
                FeedContract.TagEntry.COLUMN_PREVIEW_ID + " INTEGER NOT NULL " +
                ");";

        final String SQL_CREATE_PREVIEW_TABLE = "CREATE TABLE " + FeedContract.PreviewEntry.TABLE_NAME + " (" +

                FeedContract.PreviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                FeedContract.PreviewEntry.COLUMN_POST_ID_KEY + " INTEGER NOT NULL, " +
                FeedContract.PreviewEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FeedContract.PreviewEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
                FeedContract.PreviewEntry.COLUMN_LIKES + " INTEGER NOT NULL, " +
                FeedContract.PreviewEntry.COLUMN_IS_LIKE + " INTEGER NOT NULL, " +
                FeedContract.PreviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                FeedContract.PreviewEntry.COLUMN_AUTHOR_IMAGE + " TEXT NOT NULL, " +
                FeedContract.PreviewEntry.COLUMN_CREATE_DATE + " TEXT NOT NULL, " +
                FeedContract.PreviewEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                FeedContract.PreviewEntry.COLUMN_IS_MY + " INTEGER "

                // Set up the comment column as a foreign key to comment table.
//                + " FOREIGN KEY (" + FeedContract.PreviewEntry.COLUMN_POST_ID_KEY + ") REFERENCES " +
//                FeedContract.CommentEntry.TABLE_NAME + " (" + FeedContract.CommentEntry._ID + ")" +

                + " );";

        final String SQL_CREATE_CREATOR_TABLE = "CREATE TABLE " + FeedContract.CreatorEntry.TABLE_NAME + " (" +
                FeedContract.CreatorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FeedContract.CreatorEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                FeedContract.CreatorEntry.COLUMN_IS_ADMIN + " INTEGER NOT NULL," +
                FeedContract.CreatorEntry.COLUMN_IMAGE + " TEXT UNIQUE  " +
                ");";

        final String SQL_CREATE_PREVIEW_TAG_TABLE = "CREATE TABLE " + FeedContract.PreviewTagEntry.TABLE_NAME + " (" +
                FeedContract.PreviewTagEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FeedContract.PreviewTagEntry.COLUMN_PREVIEW_ID + " INTEGER NOT NULL, " +
                FeedContract.PreviewTagEntry.COLUMN_TAG_ID + " INTEGER NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_USER_NEWS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PROFILE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_COMMENT_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TAG_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PREVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CREATOR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PREVIEW_TAG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedContract.PreviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedContract.TagEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedContract.CommentEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedContract.CreatorEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedContract.PreviewTagEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedContract.UserNewsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedContract.UserProfileEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

