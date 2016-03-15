package com.knowroaming.czheng035.shoppinglist.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class define the content provider contract for shopping_list table
 */
public class ShoppingListContract implements BaseContract, BaseColumns {

    public static final String TABLE_NAME = "shopping_list";

    public static final String COLUMN_LIST_NAME = "list_name";
    public static final String COLUMN_USER_ID = "user_id";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

    public static Uri buildUriShoppingListById(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
