package com.knowroaming.czheng035.shoppinglist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.knowroaming.czheng035.shoppinglist.adapter.ItemSwipeAdapter;
import com.knowroaming.czheng035.shoppinglist.data.ItemContract;
import com.knowroaming.czheng035.shoppinglist.util.LocalCacheUtil;

public class ShoppingListActivity extends AppCompatActivity {

    private final static int LOADER_ITEMS_BY_SHOPPING_LIST = 0;
    private final static int LOADER_ITEMS_BY_SHOPPING_LIST_AND_ITEM_NAME = 1;

    private boolean mSaveActivityAsLast;

    private long mShoppingListId;
    private String mShoppingListName;

    private ListView mItemListView;
    private ItemSwipeAdapter mItemAdapter;

    private EditText mEdtxtItemName;
    private Button mBtnNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        mSaveActivityAsLast = true;

        initView();
    }

    private void initView() {
        mEdtxtItemName = (EditText) findViewById(R.id.edtxt_item_name);
        mBtnNewItem = (Button) findViewById(R.id.btn_new_item);
        mItemListView = (ListView) findViewById(R.id.item_list);

        mBtnNewItem.setOnClickListener(mBtnNewItemOnClickListener);
        mEdtxtItemName.addTextChangedListener(mEdtxtItemNameTextWatcher);

        mItemAdapter = new ItemSwipeAdapter(this, null, 0);
        mItemListView.setAdapter(mItemAdapter);

        mShoppingListName = getIntent().getStringExtra(getString(R.string.extra_key_shopping_list_name));
        mShoppingListId = getIntent().getLongExtra(getString(R.string.extra_key_shopping_list_id), 0);
        getSupportActionBar().setTitle(mShoppingListName);

        getSupportLoaderManager().initLoader(LOADER_ITEMS_BY_SHOPPING_LIST, null, mItemCursorLoader);
    }

    private View.OnClickListener mBtnNewItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String itemName = mEdtxtItemName.getText().toString().trim();
            if (validateItemName(itemName)) {
                AsyncTask<String, Void, Boolean> addNewItemAsyncTask
                        = new AsyncTask<String, Void, Boolean>() {
                    private String mItemName;

                    @Override
                    protected Boolean doInBackground(String... params) {
                        mItemName = params[0];

                        Uri uri = ItemContract.CONTENT_URI;
                        String selection = ItemContract.COLUMN_ITEM_NAME + "=? AND " +
                                ItemContract.COLUMN_SHOPPING_LIST_ID +"=?";
                        String[] selectionArgs = {
                                mItemName,
                                Long.toString(mShoppingListId)
                        };

                        String sortOrder = ItemContract.COLUMN_ITEM_NAME + " ASC";

                        Cursor cursor = getContentResolver().query(
                                uri,
                                null,
                                selection,
                                selectionArgs,
                                sortOrder
                        );

                        if (cursor.getCount() != 0) return false;

                        ContentValues itemValues = new ContentValues();
                        itemValues.put(ItemContract.COLUMN_ITEM_NAME, mItemName);
                        itemValues.put(ItemContract.COLUMN_SHOPPING_LIST_ID, mShoppingListId);
                        itemValues.put(ItemContract.COLUMN_ITEM_STATUS, ItemContract.ITEM_UNDONE);

                        getContentResolver().insert(uri, itemValues);
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if (result) {
                            Snackbar.make(findViewById(R.id.root_layout),
                                    R.string.msg_item_added, Snackbar.LENGTH_SHORT)
                                    .show();
                        } else {
                            Snackbar.make(findViewById(R.id.root_layout),
                                    R.string.msg_item_exist, Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                };

                addNewItemAsyncTask.execute(itemName);
            }
            mEdtxtItemName.setText(null);
        }
    };

    private boolean validateItemName(String itemName) {
        if (itemName == null || itemName.trim().equals("")) return false;
        else return true;
    }

    private TextWatcher mEdtxtItemNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            getSupportLoaderManager().restartLoader(LOADER_ITEMS_BY_SHOPPING_LIST_AND_ITEM_NAME,
                    null, mItemCursorLoader);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private LoaderManager.LoaderCallbacks<Cursor> mItemCursorLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    Uri uri = ItemContract.CONTENT_URI;

                    String[] projection = {
                            ItemContract._ID,
                            ItemContract.COLUMN_ITEM_NAME,
                            ItemContract.COLUMN_ITEM_STATUS
                    };

                    String sortOrder = ItemContract.COLUMN_ITEM_STATUS + "," +
                            ItemContract.COLUMN_ITEM_NAME + " ASC";

                    String selection = ItemContract.COLUMN_SHOPPING_LIST_ID + "=?";
                    String[] selectionArgs = {
                            Long.toString(mShoppingListId)
                    };

                    String itemName = mEdtxtItemName.getText().toString().trim();

                    if (id == LOADER_ITEMS_BY_SHOPPING_LIST_AND_ITEM_NAME && !itemName.equals("")) {
                        selection += " AND " + ItemContract.COLUMN_ITEM_NAME + " LIKE ?";
                        selectionArgs = new String[]{
                                Long.toString(mShoppingListId),
                                itemName + "%"
                        };
                    }

                    return new CursorLoader(ShoppingListActivity.this,
                            uri,
                            projection,
                            selection,
                            selectionArgs,
                            sortOrder);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    mItemAdapter.swapCursor(data);
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    mItemAdapter.swapCursor(null);
                }
            };

    @Override
    protected void onPause() {
        super.onPause();
        if (mSaveActivityAsLast) {
            LocalCacheUtil.saveLastActivity(this, this);
            LocalCacheUtil.saveLastShoppingList(this, mShoppingListName, mShoppingListId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home: {
                LocalCacheUtil.clearLastActivity(this);
                LocalCacheUtil.clearLastUser(this);
                LocalCacheUtil.clearLastShoppingList(this);
                mSaveActivityAsLast = false;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }

            case R.id.menu_my_list: {
                LocalCacheUtil.clearLastActivity(this);
                LocalCacheUtil.clearLastShoppingList(this);
                mSaveActivityAsLast = false;
                Intent intent = new Intent(this, ShoppingListsPerUserActivity.class);
                String userId = LocalCacheUtil.getLastUser(this);
                if (userId != null) {
                    intent.putExtra(getString(R.string.extra_key_user_id), userId);
                }
                startActivity(intent);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
