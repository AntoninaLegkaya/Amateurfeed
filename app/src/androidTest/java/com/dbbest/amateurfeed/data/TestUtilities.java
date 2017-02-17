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
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;
import android.util.Log;


import java.util.Map;
import java.util.Set;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your WeatherContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {



    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            Log.i("TestProvider", "Column: " + columnName + " : " + entry.getValue().toString());
//            assertEquals("Value '" + entry.getValue().toString() +
//                    "' did not match the expected value '" +
//                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }




    static ContentValues createPreviewValues() {
        ContentValues previewValues = new ContentValues();
        previewValues.put(FeedContract.PreviewEntry.COLUMN_TITLE, "Preview 1");
        previewValues.put(FeedContract.PreviewEntry.COLUMN_TEXT, "Some text preview");
        previewValues.put(FeedContract.PreviewEntry.COLUMN_LIKES, 5);
        previewValues.put(FeedContract.PreviewEntry.COLUMN_IS_LIKE, 0);
        previewValues.put(FeedContract.PreviewEntry.COLUMN_AUTHOR, "Tony");
        previewValues.put(FeedContract.PreviewEntry.COLUMN_AUTHOR_IMAGE, "http://gitlab.kharkov.dbbest.com/lehka.a/Less12/blob/0974a07098cc4b715b3189651d7019439341564e/app/src/main/res/mipmap-hdpi/ic_launcher.png");
        previewValues.put(FeedContract.PreviewEntry.COLUMN_CREATE_DATE, "30 January 2017");
        previewValues.put(FeedContract.PreviewEntry.COLUMN_IMAGE, "http://gitlab.kharkov.dbbest.com/lehka.a/Less12/blob/0974a07098cc4b715b3189651d7019439341564e/app/src/main/res/mipmap-hdpi/ic_launcher.png");
        previewValues.put(FeedContract.PreviewEntry.COLUMN_IS_MY, 1);
        return previewValues;
    }


    static ContentValues createPreviewTagValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(FeedContract.PreviewTagEntry.COLUMN_PREVIEW_ID, FeedProvider.TEST_ID);
        testValues.put(FeedContract.PreviewTagEntry.COLUMN_TAG_ID, 1);
        return testValues;
    }

    static ContentValues createTagValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(FeedContract.TagEntry.COLUMN_NAME, "#tag1");
        return testValues;
    }


    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }


        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }


    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
