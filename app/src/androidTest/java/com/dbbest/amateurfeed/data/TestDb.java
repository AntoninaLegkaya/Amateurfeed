/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dbbest.amateurfeed.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();




    public void setUp() {
        deleteTheDatabase();
    }
    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(FeedDbHelper.DATABASE_NAME);
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(FeedContract.PreviewEntry.TABLE_NAME);
        tableNameHashSet.add(FeedContract.CommentEntry.TABLE_NAME);
        tableNameHashSet.add(FeedContract.PreviewTagEntry.TABLE_NAME);
        tableNameHashSet.add(FeedContract.TagEntry.TABLE_NAME);
        tableNameHashSet.add(FeedContract.UserProfileEntry.TABLE_NAME);
        tableNameHashSet.add(FeedContract.UserNewsEntry.TABLE_NAME);


        mContext.deleteDatabase(FeedDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new FeedDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the preview entry....tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + FeedContract.PreviewEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> previewColumnHashSet = new HashSet<String>();
        previewColumnHashSet.add(FeedContract.PreviewEntry._ID);
        previewColumnHashSet.add(FeedContract.PreviewEntry.COLUMN_TITLE);
        previewColumnHashSet.add(FeedContract.PreviewEntry.COLUMN_TEXT);
        previewColumnHashSet.add(FeedContract.PreviewEntry.COLUMN_LIKES);
        previewColumnHashSet.add(FeedContract.PreviewEntry.COLUMN_IS_LIKE);
        previewColumnHashSet.add(FeedContract.PreviewEntry.COLUMN_AUTHOR);
        previewColumnHashSet.add(FeedContract.PreviewEntry.COLUMN_AUTHOR_IMAGE);
        previewColumnHashSet.add(FeedContract.PreviewEntry.COLUMN_CREATE_DATE);
        previewColumnHashSet.add(FeedContract.PreviewEntry.COLUMN_IMAGE);
        previewColumnHashSet.add(FeedContract.PreviewEntry.COLUMN_IS_MY);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            previewColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required preview
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required preview entry columns",
                previewColumnHashSet.isEmpty());
        db.close();
    }


    public void testPreviewTable() {


        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        FeedDbHelper dbHelper = new FeedDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step (Preview): Create preview values
        ContentValues previewValues = TestUtilities.createPreviewValues();

        // Third Step (preview): Insert ContentValues into database and get a row ID back
        long previewRowId = db.insert(FeedContract.PreviewEntry.TABLE_NAME, null, previewValues);
        assertTrue(previewRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor previewCursor = db.query(
                FeedContract.PreviewEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from preview query", previewCursor.moveToFirst());

        // Fifth Step: Validate
        TestUtilities.validateCurrentRecord("testInsertReadDb previewEntry failed to validate",
                previewCursor, previewValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from preview query",
                previewCursor.moveToNext());

        // Sixth Step: Close cursor and database
        previewCursor.close();
        dbHelper.close();
    }


}
