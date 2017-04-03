package com.dbbest.amateurfeed.data;

import android.net.Uri;


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

  public static final String SQL_CREATE_USER_NEWS_TABLE =
      "CREATE TABLE " + UserNewsEntry.TABLE_NAME + " (" +
          UserNewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
          UserNewsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
          UserNewsEntry.COLUMN_UPDATE_DATE + " TEXT NOT NULL, " +
          UserNewsEntry.COLUMN_STATUS + " TEXT NOT NULL, " +
          UserNewsEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
          UserNewsEntry.COLUMN_LIKES + " INTEGER " +
          " );";


  public static final String SQL_CREATE_PROFILE_USER_TABLE =
      "CREATE TABLE " + UserProfileEntry.TABLE_NAME + " (" +
          UserProfileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
          UserProfileEntry.COLUMN_FULL_NAME + " TEXT  NOT NULL, " +
          UserProfileEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
          UserProfileEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
          UserProfileEntry.COLUMN_SKYPE + " TEXT NOT NULL, " +
          UserProfileEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
          UserProfileEntry.COLUMN_JOB + " TEXT NOT NULL, " +
          UserProfileEntry.COLUMN_PHONE + " TEXT NOT NULL" +
          " );";

  public static final String SQL_CREATE_COMMENT_USER_TABLE =
      "CREATE TABLE " + CommentEntry.TABLE_NAME + " (" +
          CommentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
          CommentEntry.COLUMN_POST_ID + " INTEGER NOT NULL, " +
          CommentEntry.COLUMN_CREATOR_KEY + " INTEGER NOT NULL, " +
          CommentEntry.COLUMN_BODY + " TEXT  NOT NULL, " +
          CommentEntry.COLUMN_PARENT_COMMENT_ID + " INTEGER NOT NULL, " +
          CommentEntry.COLUMN_CREATE_DATE + " TEXT  NOT NULL, " +
          " FOREIGN KEY (" + CommentEntry.COLUMN_CREATOR_KEY + ") REFERENCES " +
          CreatorEntry.TABLE_NAME + " (" + CreatorEntry._ID + ")" +
          ");";

  public static final String SQL_CREATE_TAG_TABLE = "CREATE TABLE " + TagEntry.TABLE_NAME + " (" +
      TagEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
      TagEntry.COLUMN_NAME + " TEXT NOT NULL, " +
      TagEntry.COLUMN_TAG_ID + " INTEGER NOT NULL, " +
      TagEntry.COLUMN_PREVIEW_ID + " INTEGER NOT NULL " +
      ");";

  public static final String SQL_CREATE_PREVIEW_TABLE =
      "CREATE TABLE " + PreviewEntry.TABLE_NAME + " (" +
          PreviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
          PreviewEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
          PreviewEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
          PreviewEntry.COLUMN_LIKES + " INTEGER NOT NULL, " +
          PreviewEntry.COLUMN_IS_LIKE + " INTEGER NOT NULL, " +
          PreviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
          PreviewEntry.COLUMN_AUTHOR_IMAGE + " TEXT NOT NULL, " +
          PreviewEntry.COLUMN_CREATE_DATE + " TEXT NOT NULL, " +
          PreviewEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
          PreviewEntry.COLUMN_IS_MY + " INTEGER "
          + " );";

  public static final String SQL_CREATE_CREATOR_TABLE =
      "CREATE TABLE " + CreatorEntry.TABLE_NAME + " (" +
          CreatorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
          CreatorEntry.COLUMN_NAME + " TEXT  NOT NULL, " +
          CreatorEntry.COLUMN_IS_ADMIN + " INTEGER NOT NULL," +
          CreatorEntry.COLUMN_IMAGE + " TEXT   " +
          ");";


}
