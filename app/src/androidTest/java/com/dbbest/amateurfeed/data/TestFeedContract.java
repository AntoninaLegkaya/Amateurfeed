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
package  com.dbbest.amateurfeed.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/*
    Students: This is NOT a complete test for the WeatherContract --- just for the functions
    that we expect you to write.
 */
public class TestFeedContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    private static final String TEST_PREVIEW_IS_MY = "/1";


    public void testBuildWeatherLocation() {
        Uri getIsMyFlagUri = FeedContract.PreviewEntry.buildIsMyPreview(TEST_PREVIEW_IS_MY);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildPreview in " +
                        "PreviewContract.",
                getIsMyFlagUri);
        assertEquals("Error: Preview id not properly appended to the end of the Uri",
                TEST_PREVIEW_IS_MY, getIsMyFlagUri.getLastPathSegment());
        assertEquals("Error: Preview  Uri doesn't match our expected result",
                getIsMyFlagUri.toString(),
                "content://com.dbbest.amateurfeed.app/preview/%2F1");
    }
}
