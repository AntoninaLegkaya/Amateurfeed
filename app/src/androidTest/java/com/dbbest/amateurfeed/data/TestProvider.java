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

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;


public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /*
       This helper function deletes all records from both database tables using the ContentProvider.
       It also queries the ContentProvider to make sure that the database has been successfully
       deleted, so it cannot be used until the Query and Delete functions have been written
       in the ContentProvider.

       Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
       the delete functionality in the ContentProvider.
     */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                FeedContract.PreviewEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                FeedContract.PreviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Preview table during delete", 0, cursor.getCount());
        cursor.close();


    }

    /*
        Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    /*
        This test checks to make sure that the content provider is registered correctly.
        Students: Uncomment this test to make sure you've correctly registered the WeatherProvider.
     */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                FeedProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + FeedContract.AUTHORITY,
                    providerInfo.authority, FeedContract.AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }


    public void testGetType() {
        // content://com.example.android.sunshine.app/weather/
        String type = mContext.getContentResolver().getType(FeedContract.PreviewEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.dbbest.amateurfeed.app/preview
        assertEquals("Error: the PreviewEntry CONTENT_URI should return PreviewEntry.CONTENT_TYPE",
                FeedContract.PreviewEntry.CONTENT_TYPE, type);


    }


    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic weather query functionality
        given in the ContentProvider is working correctly.
     */
    public void testBasicPreviewQuery() {
        // insert our test records into the database
        FeedDbHelper dbHelper = new FeedDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues previewValues = TestUtilities.createPreviewValues();

        long previewRowId = db.insert(FeedContract.PreviewEntry.TABLE_NAME, null, previewValues);
        assertTrue("Unable to Insert previewEntry into the Database", previewRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor previewCursor = mContext.getContentResolver().query(
                FeedContract.PreviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicPreviewQuery", previewCursor, previewValues);


    }

    public void testBasicTagQuery() {

        // insert our test records into the database
        FeedDbHelper dbHelper = new FeedDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues tagValues = TestUtilities.createTagValues();

        long tagRowId = db.insert(FeedContract.TagEntry.TABLE_NAME, null, tagValues);
        assertTrue("Unable to Insert TagEntry into the Database", tagRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor tagCursor = mContext.getContentResolver().query(
                FeedContract.TagEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicTagQuery", tagCursor, tagValues);

    }

    public void testBasicPreviewTagQuery() {

        // insert our test records into the database
        FeedDbHelper dbHelper = new FeedDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues previewTagValues = TestUtilities.createPreviewTagValues();

        long previewTagRowId = db.insert(FeedContract.PreviewTagEntry.TABLE_NAME, null, previewTagValues);
        assertTrue("Unable to Insert PreviewTagEntry into the Database", previewTagRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor previewTagCursor = mContext.getContentResolver().query(
                FeedContract.PreviewTagEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicPreviewTagQuery", previewTagCursor, previewTagValues);

    }

    public void testInsertReadProvider() {


        /**Preview content Table**/

        ContentValues previewValues = TestUtilities.createPreviewValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FeedContract.PreviewEntry.CONTENT_URI, true, tco);
        Uri previewUri = mContext.getContentResolver().insert(FeedContract.PreviewEntry.CONTENT_URI, previewValues);

        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long previewRowId = ContentUris.parseId(previewUri);

        // Verify we got a row back.
        assertTrue(previewRowId != -1);


        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                FeedContract.PreviewEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        Log.i("TestProvider", "---------------------------Preview Table--------------------------------");
        TestUtilities.validateCursor("testInsertReadProvider. Error validating PreviewEntry.",
                cursor, previewValues);
        Log.i("TestProvider", "------------------------------------------------------------------------");


        /**Tag Content values**/

        ContentValues tagValues = TestUtilities.createTagValues();
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(FeedContract.TagEntry.CONTENT_URI, true, tco);

        Uri tagInsertUri = mContext.getContentResolver()
                .insert(FeedContract.TagEntry.CONTENT_URI, tagValues);
        assertTrue(tagInsertUri != null);


        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor tagCursor = mContext.getContentResolver().query(
                FeedContract.TagEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        Log.i("TestProvider", "---------------------------Tag Table--------------------------------");
        TestUtilities.validateCursor("testInsertReadProvider. Error validating TagEntry insert.",
                tagCursor, tagValues);
        Log.i("TestProvider", "------------------------------------------------------------------------");


        /** Preview_Tag Content values **/

        ContentValues previewTagValues = TestUtilities.createPreviewTagValues();
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(FeedContract.PreviewTagEntry.CONTENT_URI, true, tco);

        Uri previewTagInsertUri = mContext.getContentResolver()
                .insert(FeedContract.PreviewTagEntry.CONTENT_URI, previewTagValues);
        assertTrue(previewTagInsertUri != null);


        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor previewTagCursor = mContext.getContentResolver().query(
                FeedContract.PreviewTagEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        Log.i("TestProvider", "---------------------------Preview_Tag Table--------------------------------");
        TestUtilities.validateCursor("testInsertReadProvider. Error validating TagEntry insert.",
                previewTagCursor, previewTagValues);
        Log.i("TestProvider", "------------------------------------------------------------------------");


        Uri uri = FeedContract.PreviewTagEntry.buildTagUriById(FeedProvider.TEST_ID);
        Log.i("TestProvider", uri.toString());
        Cursor byIdPreviewCursor = mContext.getContentResolver().query(
                uri,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );


        TestUtilities.validateCursor("testInsertReadProvider.  Error validating  Preview by Id request.",
                byIdPreviewCursor, previewValues);


/** Test get Tag item by _ID**/

        Uri uriTagId = FeedContract.TagEntry.buildTagUriById(FeedProvider.TEST_TAG_ID);
        Log.i("TestProvider", uriTagId.toString());
        Cursor byIdTagCursor = mContext.getContentResolver().query(
                uriTagId,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );


        TestUtilities.validateCursor("testInsertReadProvider.  Error validating  Tag by Id request.",
                byIdTagCursor, tagValues);

/** Test get preview by Author**/

        Uri uriPreviewAuthor = FeedContract.PreviewEntry.buildPreviewUriByAuthor(FeedProvider.TEST_TAG_AUTHOR);
        Log.i("TestProvider", uriPreviewAuthor.toString());
        Cursor byAuthorPreviewCursor = mContext.getContentResolver().query(
                uriPreviewAuthor,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );


        TestUtilities.validateCursor("testInsertReadProvider.  Error validating  Preview by Author request.",
                byAuthorPreviewCursor, previewValues);





    }


}
