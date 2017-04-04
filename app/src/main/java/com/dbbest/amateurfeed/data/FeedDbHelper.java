package com.dbbest.amateurfeed.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedDbHelper extends SQLiteOpenHelper {

  public static final String DATABASE_NAME = "feed.db";
  private static final int DATABASE_VERSION = 2;

  public FeedDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(FeedContract.SQL_CREATE_USER_NEWS_TABLE);
    sqLiteDatabase.execSQL(FeedContract.SQL_CREATE_PROFILE_USER_TABLE);
    sqLiteDatabase.execSQL(FeedContract.SQL_CREATE_COMMENT_USER_TABLE);
    sqLiteDatabase.execSQL(FeedContract.SQL_CREATE_TAG_TABLE);
    sqLiteDatabase.execSQL(FeedContract.SQL_CREATE_PREVIEW_TABLE);
    sqLiteDatabase.execSQL(FeedContract.SQL_CREATE_CREATOR_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PreviewEntry.TABLE_NAME);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TagEntry.TABLE_NAME);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CommentEntry.TABLE_NAME);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CreatorEntry.TABLE_NAME);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserNewsEntry.TABLE_NAME);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserProfileEntry.TABLE_NAME);
    onCreate(sqLiteDatabase);
  }
}

