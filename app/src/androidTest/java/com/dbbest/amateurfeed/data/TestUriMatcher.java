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

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;


public class TestUriMatcher extends AndroidTestCase {

  // content://com.dbbest.amateurfeed.app/preview"
  private static final Uri TEST_PREVIEW_DIR = PreviewEntry.CONTENT_URI;
  // content://com.dbbest.amateurfeed.app/comment"
  private static final Uri TEST_COMMENT_DIR = CommentEntry.CONTENT_URI;
  private static final Uri TEST_PREVIEW_ITEM = PreviewEntry.buildPreviewUriById(FeedProvider.TEST_ID);


  public void testUriMatcher() {
    UriMatcher testMatcher = FeedProvider.buildUriMatcher();

    assertEquals("Error: The PREVIEW URI was matched incorrectly.",
        testMatcher.match(TEST_PREVIEW_DIR), FeedProvider.PREVIEW);
    assertEquals("Error: The COMMENT URI was matched incorrectly.",
        testMatcher.match(TEST_COMMENT_DIR), FeedProvider.COMMENT);
    assertEquals("Error: The PREVIEW URI By ID query was matched incorrectly.",
        testMatcher.match(TEST_PREVIEW_ITEM), FeedProvider.PREVIEW_ID);

  }
}
