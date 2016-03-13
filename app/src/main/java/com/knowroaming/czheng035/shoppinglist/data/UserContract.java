package com.knowroaming.czheng035.shoppinglist.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by santa on 09/03/16.
 */
public class UserContract implements BaseContract, BaseColumns {

    public static final String TABLE_NAME = "user";

    public static final String COLUMN_USER_ID = "user_id";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

    public static Uri buildUriUserById(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
