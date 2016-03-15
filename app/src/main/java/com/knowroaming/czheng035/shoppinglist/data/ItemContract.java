package com.knowroaming.czheng035.shoppinglist.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class defines the content provider contract for item table
 */
public class ItemContract implements BaseContract, BaseColumns {

    public static final int ITEM_UNDONE = 0;
    public static final int ITEM_DONE = 1;

    public static final String TABLE_NAME = "item";

    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ITEM_STATUS = "item_status";
    public static final String COLUMN_SHOPPING_LIST_ID = "shopping_list_id";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

    public static Uri buildUriItemById(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
