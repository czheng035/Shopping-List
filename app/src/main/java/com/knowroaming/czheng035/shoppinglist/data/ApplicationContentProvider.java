package com.knowroaming.czheng035.shoppinglist.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * This is the custom content provider which provides CRUD operations to the
 * user, shopping list and item tables
 */
public class ApplicationContentProvider extends ContentProvider {

    private static final int USER = 1;
    private static final int SHOPPING_LIST = 2;
    private static final int ITEM = 3;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BaseContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, UserContract.TABLE_NAME, USER);
        matcher.addURI(authority, ShoppingListContract.TABLE_NAME, SHOPPING_LIST);
        matcher.addURI(authority, ItemContract.TABLE_NAME, ITEM);

        return matcher;
    }

    private DatabaseHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case USER: {
                retCursor = db.query(
                        UserContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case SHOPPING_LIST: {
                retCursor = db.query(
                        ShoppingListContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case ITEM: {
                retCursor = db.query(
                        ItemContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case USER:
                return UserContract.CONTENT_TYPE;
            case SHOPPING_LIST:
                return ShoppingListContract.CONTENT_TYPE;
            case ITEM:
                return ItemContract.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case USER: {
                long id = db.insert(UserContract.TABLE_NAME, null, values);
                if ( id > 0 )
                    returnUri = UserContract.buildUriUserById(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case SHOPPING_LIST: {
                long id = db.insert(ShoppingListContract.TABLE_NAME, null, values);
                if ( id > 0 )
                    returnUri = ShoppingListContract.buildUriShoppingListById(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case ITEM: {
                long id = db.insert(ItemContract.TABLE_NAME, null, values);
                if ( id > 0 )
                    returnUri = ItemContract.buildUriItemById(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case ITEM:
                rowsDeleted = db.delete(
                        ItemContract.TABLE_NAME, selection, selectionArgs);
                break;
            case SHOPPING_LIST:
                rowsDeleted = db.delete(
                        ShoppingListContract.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case ITEM:
                rowsUpdated = db.update(ItemContract.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
