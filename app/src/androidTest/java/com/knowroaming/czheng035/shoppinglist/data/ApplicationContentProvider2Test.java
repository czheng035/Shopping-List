package com.knowroaming.czheng035.shoppinglist.data;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.ProviderTestCase2;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

public class ApplicationContentProvider2Test extends ProviderTestCase2<ApplicationContentProvider> {

    private final static String TEST_USER_ID = "john";
    private final static String TEST_SHOPPING_LIST_NAME = "Thanksgiving shopping";
    private final static String TEST_ITEM_NAME = "Turkey";

    private ContentResolver mContentResolver;

    public ApplicationContentProvider2Test() {
        super(ApplicationContentProvider.class, BaseContract.CONTENT_AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mContentResolver = getMockContentResolver();
    }

    /**
     * Test the content provider is registered correctly
     * @throws Exception
     */
    public void testProviderRegistry() throws Exception {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                ApplicationContentProvider.class.getName());

        ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

        assertThat("Error: ApplicationContentProvider registered with authority: " + providerInfo.authority +
                        " instead of authority: " + BaseContract.CONTENT_AUTHORITY,
                providerInfo.authority, is(equalTo(BaseContract.CONTENT_AUTHORITY)));
    }

    /**
     * Test the getType method of a content provider
     */
    public void testGetType() {

        String type = mContentResolver.getType(UserContract.CONTENT_URI);
        assertThat("Error: the UserContract CONTENT_URI should return UserContract.CONTENT_TYPE",
                UserContract.CONTENT_TYPE, is(equalTo(type)));

        type = mContentResolver.getType(ShoppingListContract.CONTENT_URI);
        assertThat("Error: the ShoppingListContract CONTENT_URI should return ShoppingListContract.CONTENT_TYPE",
                ShoppingListContract.CONTENT_TYPE, is(equalTo(type)));

        type = mContentResolver.getType(ItemContract.CONTENT_URI);
        assertThat("Error: the ItemContract CONTENT_URI should return ItemContract.CONTENT_TYPE",
                ItemContract.CONTENT_TYPE, is(equalTo(type)));
    }

    /**
     * Test case when insert a user and check if the record is there.
     */
    public void testInsertUser() {
        insertUserJohn();

        ContentValues userValues = createUserJohn();

        Cursor userCursor = mContentResolver.query(
                UserContract.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertThat("Error: No Records returned from user query", userCursor.moveToFirst(),
                is(equalTo(true)));

        validateCurrentRecord("testInsertUser failed to validate",
                userCursor, userValues);

        assertThat("Error: More than one record returned from user query", userCursor.moveToNext(),
                is(equalTo(false)));
    }

    /**
     * Test case for item update
     */
    public void testUpdateItemStatus() {
        long itemId = insertItemTurkey();

        ContentValues testValues = new ContentValues();
        testValues.put(ItemContract.COLUMN_ITEM_STATUS, ItemContract.ITEM_DONE);

        String selection = ItemContract._ID + "=?";
        String[] selectionArgs = {
                Long.toString(itemId)
        };

        int count = mContentResolver.update(ItemContract.CONTENT_URI,
                testValues,
                selection,
                selectionArgs
        );

        assertThat("Error: Updated number of Item Records is not 1", count, is(equalTo(1)));

        Cursor cursor = mContentResolver.query(ItemContract.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);

        assertThat("Error: No Records returned from item query", cursor.moveToFirst(),
                is(equalTo(true)));

        int itemStatusColIdx = cursor.getColumnIndex(ItemContract.COLUMN_ITEM_STATUS);

        assertThat("Error: Failed to update item status", cursor.getInt(itemStatusColIdx),
                is(equalTo(ItemContract.ITEM_DONE)));
    }

    /**
     * Test case for shopping list delete
     */
    public void testDeleteShoppingList() {
        long shoppingListId = insertThanksgivingShoppingList();

        String selection = ShoppingListContract._ID + "=?";
        String[] selectionArgs = {
                Long.toString(shoppingListId)
        };

        int count = mContentResolver.delete(ShoppingListContract.CONTENT_URI,
                selection,
                selectionArgs
        );

        assertThat("Error: Deleted number of Shopping List Records is not 1", count, is(equalTo(1)));

        Cursor cursor = mContentResolver.query(ShoppingListContract.CONTENT_URI,
                null,
                null,
                null,
                null);

        assertThat("Error: Failed to update item status", cursor.moveToFirst(), is(equalTo(false)));
    }

    private void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertThat("Column '" + columnName + "' not found. " + error, idx,
                    is(not(equalTo(-1))));
            String expectedValue = entry.getValue().toString();
            assertThat("Value '" + entry.getValue().toString() +
                            "' did not match the expected value '" +
                            expectedValue + "'. " + error, valueCursor.getString(idx),
                    is(equalTo(expectedValue)));
        }
    }

    private ContentValues createUserJohn() {
        ContentValues testValues = new ContentValues();
        testValues.put(UserContract.COLUMN_USER_ID, TEST_USER_ID);
        return testValues;
    }

    private ContentValues createThanksgivingShoppingList(String userId) {
        ContentValues testValues = new ContentValues();
        testValues.put(ShoppingListContract.COLUMN_USER_ID, userId);
        testValues.put(ShoppingListContract.COLUMN_LIST_NAME, TEST_SHOPPING_LIST_NAME);
        return testValues;
    }

    private ContentValues createItemTurkey(long shoppingListId) {
        ContentValues testValues = new ContentValues();
        testValues.put(ItemContract.COLUMN_SHOPPING_LIST_ID, shoppingListId);
        testValues.put(ItemContract.COLUMN_ITEM_NAME, TEST_ITEM_NAME);
        testValues.put(ItemContract.COLUMN_ITEM_STATUS, ItemContract.ITEM_UNDONE);
        return testValues;
    }

    private long insertUserJohn() {
        ContentValues testValues = createUserJohn();

        long userRowId;
        userRowId = ContentUris.parseId(mContentResolver.insert(UserContract.CONTENT_URI, testValues));

        assertThat("Error: Failure to insert User Values", userRowId,
                is(not(equalTo(-1L))));

        return userRowId;
    }


    private long insertThanksgivingShoppingList() {
        insertUserJohn();
        ContentValues testValues = createThanksgivingShoppingList(TEST_USER_ID);
        long shoppingListId = ContentUris
                .parseId(mContentResolver.insert(ShoppingListContract.CONTENT_URI, testValues));
        assertThat("Error: Failure to insert Shopping list Values", shoppingListId,
                is(not(equalTo(-1L))));
        return shoppingListId;
    }

    private long insertItemTurkey() {
        long shoppingListId = insertThanksgivingShoppingList();
        ContentValues testValues = createItemTurkey(shoppingListId);
        long tukeyId = ContentUris
                .parseId(mContentResolver.insert(ItemContract.CONTENT_URI, testValues));
        assertThat("Error: Failure to insert Item Values", tukeyId,
                is(not(equalTo(-1L))));
        return tukeyId;
    }
}