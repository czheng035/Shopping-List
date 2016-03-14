package com.knowroaming.czheng035.shoppinglist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "shoppinglist.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserContract.TABLE_NAME + " (" +
                UserContract._ID + " INTEGER PRIMARY KEY," +

                UserContract.COLUMN_USER_ID + " TEXT UNIQUE NOT NULL);";

        final String SQL_CREATE_SHOPPING_LIST_TABLE = "CREATE TABLE " + ShoppingListContract.TABLE_NAME + " (" +
                ShoppingListContract._ID + " INTEGER PRIMARY KEY," +

                ShoppingListContract.COLUMN_LIST_NAME + " TEXT NOT NULL, " +
                ShoppingListContract.COLUMN_USER_ID + " TEXT NOT NULL, " +

                "FOREIGN KEY (" + ShoppingListContract.COLUMN_USER_ID + ") REFERENCES " +
                UserContract.TABLE_NAME + " (" + UserContract.COLUMN_USER_ID + "), " +

                "UNIQUE (" + ShoppingListContract.COLUMN_USER_ID + ", " +
                ShoppingListContract.COLUMN_LIST_NAME + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_ITEM_TABLE = "CREATE TABLE " + ItemContract.TABLE_NAME + " (" +
                ItemContract._ID + " INTEGER PRIMARY KEY," +

                ItemContract.COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
                ItemContract.COLUMN_ITEM_STATUS + " INTEGER NOT NULL, " +
                ItemContract.COLUMN_SHOPPING_LIST_ID + " INTEGER NOT NULL, " +

                " FOREIGN KEY (" + ItemContract.COLUMN_SHOPPING_LIST_ID + ") REFERENCES " +
                ShoppingListContract.TABLE_NAME + " (" + ShoppingListContract._ID + ") ON DELETE CASCADE, " +

                " UNIQUE (" + ItemContract.COLUMN_SHOPPING_LIST_ID + ", " +
                ItemContract.COLUMN_ITEM_NAME + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_SHOPPING_LIST_TABLE);
        db.execSQL(SQL_CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ItemContract.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ShoppingListContract.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserContract.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
