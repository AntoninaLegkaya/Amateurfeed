package com.dbbest.amateurfeed.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by antonina on 27.01.17.
 */

public class FeedContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.dbbest.amateurfeed.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_PREVIEW = "preview";
    public static final String PATH_COMMENT = "comment";
    public static final String PATH_TAG = "tag";
    public static final String PATH_PREVIEW_TAG = "preview_tag";
    public static final String PATH_CREATOR = "creator";
    public static final String PATH_USER_NEWS = "user_news";
    public static final String PATH_PROFILE = "profile";


    /* User News Entry*/

    public static final class UserNewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_NEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER_NEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER_NEWS;

        // Table name
        public static final String TABLE_NAME = "user_news";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_UPDATE_DATE = "update_date";

        public static final String COLUMN_STATUS = "status";

        public static final String COLUMN_IMAGE = "image";

        public static final String COLUMN_LIKES = "likes";


        public static Uri buildUserNewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* User Profile  Entry*/

    public static final class UserProfileEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROFILE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILE;

        // Table name
        public static final String TABLE_NAME = "profile_user";

        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_EMAIL = "email";


        public static final String COLUMN_IMAGE = "image";

        public static final String COLUMN_SKYPE = "skype";

        public static final String COLUMN_ADDRESS = "address";

        public static final String COLUMN_JOB = "job";

        public static final String COLUMN_PHONE = "phone";


        public static Uri buildUserProfileUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Comment Entry*/

    public static final class CommentEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMENT;

        // Table name
        public static final String TABLE_NAME = "comment";

        // Column with the foreign key into the creator table.
        public static final String COLUMN_CREATOR_KEY = "creator_id";

//        // Column with the foreign key into the comment table.
//        public static final String COLUMN_COMMENT_KEY = "comment_child_id";

        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_PARENT_COMMENT_ID = "parent_comment_id";
        public static final String COLUMN_CREATOR_ID = "creator_id";
        public static final String COLUMN_CREATE_DATE = "create_date";


        public static Uri buildCommentUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Tag Entry*/

    public static final class TagEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TAG).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TAG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TAG;

        // Table name
        public static final String TABLE_NAME = "tag";

        public static final String COLUMN_NAME = "name";


        public static Uri buildTagUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Preview Entry*/

    /* Inner class that defines the table contents of the preview table */
    public static final class PreviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PREVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PREVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PREVIEW;

        // Table name
        public static final String TABLE_NAME = "preview";

        //        // Column with the foreign key into the tag table.
//        public static final String COLUMN_TAG_KEY = "tag_id";
//
        // Column with the foreign key into the comment table.
        public static final String COLUMN_COMMENT_KEY = "post_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_TEXT = "text";

        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_IS_LIKE = "is_like";


        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_AUTHOR_IMAGE = "author_image";

        public static final String COLUMN_CREATE_DATE = "create_date";

        public static final String COLUMN_IMAGE = "image";

        public static final String COLUMN_IS_MY = "is_my";

        public static Uri buildPreviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Creator Entry*/

    public static final class CreatorEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CREATOR).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CREATOR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CREATOR;

        // Table name
        public static final String TABLE_NAME = "creator";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_IS_ADMIN = "is_admin";

        public static final String COLUMN_IMAGE = "image";


        public static Uri buildCreatorUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Preview_Tag Entry*/

    public static final class PreviewTagEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PREVIEW_TAG).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PREVIEW_TAG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PREVIEW_TAG;

        // Table name
        public static final String TABLE_NAME = "preview_tag";
        public static final String COLUMN_PREVIEW_ID = "preview_id";
        public static final String COLUMN_TAG_ID = "tag_id";


        public static Uri buildPreviewTagUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }





}
