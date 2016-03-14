package com.knowroaming.czheng035.shoppinglist;

import android.content.ContentUris;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.knowroaming.czheng035.shoppinglist.adapter.ShoppingListSwipeAdapter;
import com.knowroaming.czheng035.shoppinglist.data.ShoppingListContract;
import com.knowroaming.czheng035.shoppinglist.util.CommonUtil;
import com.knowroaming.czheng035.shoppinglist.util.LocalCacheUtil;

public class ShoppingListsPerUserActivity extends AppCompatActivity {

    private final static int LOADER_SHOPPING_LIST_BY_USER = 0;
    private final static int LOADER_SHOPPING_LIST_BY_USER_AND_NAME = 1;

    private boolean mSaveActivityAsLast;

    private String mUserId;

    private EditText mEdtxtShoppingListName;
    private Button mBtnNewShoppingList;

    private ListView mLvShoppingList;
    private ShoppingListSwipeAdapter mShoppingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_lists_per_user);

        mSaveActivityAsLast = true;

        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSaveActivityAsLast) {
            LocalCacheUtil.saveLastActivity(this, this);
            LocalCacheUtil.saveLastUser(this, mUserId);
        }
    }

    private void initView() {
        mEdtxtShoppingListName = (EditText) findViewById(R.id.edtxt_shopping_list_name);
        mBtnNewShoppingList = (Button) findViewById(R.id.btn_new_shopping_list);
        mLvShoppingList = (ListView) findViewById(R.id.shopping_lists);

        mEdtxtShoppingListName.addTextChangedListener(mEdtxtShoppingListNameTextWatcher);
        mBtnNewShoppingList.setOnClickListener(mBtnNewShoppingListOnClickListener);

        mShoppingListAdapter = new ShoppingListSwipeAdapter(this, null, 0);
        mLvShoppingList.setAdapter(mShoppingListAdapter);
        mLvShoppingList.setOnItemClickListener(mLvShoppingListOnItemClickListener);

        mUserId = getIntent().getStringExtra(getString(R.string.extra_key_user_id));
        getSupportActionBar().setTitle(mUserId + "\'s Lists");

        getSupportLoaderManager().initLoader(LOADER_SHOPPING_LIST_BY_USER, null, mShoppingListCursorLoader);
    }

    private TextWatcher mEdtxtShoppingListNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            getSupportLoaderManager().restartLoader(LOADER_SHOPPING_LIST_BY_USER_AND_NAME,
                    null, mShoppingListCursorLoader);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private View.OnClickListener mBtnNewShoppingListOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CommonUtil.hideSoftKeyboard(mEdtxtShoppingListName, ShoppingListsPerUserActivity.this);

            String shoppingListName = mEdtxtShoppingListName.getText().toString().trim();
            if (CommonUtil.validateUserInput(shoppingListName)) {
                AsyncTask<String, Void, Boolean> addNewShoppingListAsyncTask
                        = new AsyncTask<String, Void, Boolean>() {
                    private String mShoppingListName;
                    private long mShoppingListId;

                    @Override
                    protected Boolean doInBackground(String... params) {
                        mShoppingListName = params[0];

                        Uri uri = ShoppingListContract.CONTENT_URI;
                        String selection = ShoppingListContract.COLUMN_USER_ID + "=? AND " +
                                ShoppingListContract.COLUMN_LIST_NAME +"=?";
                        String[] selectionArgs = {
                                mUserId,
                                mShoppingListName
                        };

                        String sortOrder = ShoppingListContract.COLUMN_LIST_NAME + " ASC";

                        Cursor cursor = getContentResolver().query(
                                uri,
                                null,
                                selection,
                                selectionArgs,
                                sortOrder
                        );

                        if (cursor.getCount() != 0) return false;

                        ContentValues shoppingListValues = new ContentValues();
                        shoppingListValues.put(ShoppingListContract.COLUMN_LIST_NAME, mShoppingListName);
                        shoppingListValues.put(ShoppingListContract.COLUMN_USER_ID, mUserId);

                        Uri retUri = getContentResolver().insert(uri, shoppingListValues);
                        mShoppingListId = ContentUris.parseId(retUri);

                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if (result) {
                            startShoppingListActivity(mShoppingListName, mShoppingListId);
                        } else {
                            Snackbar.make(findViewById(R.id.root_layout),
                                    R.string.msg_shopping_list_exist, Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                };

                addNewShoppingListAsyncTask.execute(shoppingListName);
            }
            mEdtxtShoppingListName.setText(null);
        }
    };

    private AdapterView.OnItemClickListener mLvShoppingListOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = mShoppingListAdapter.getCursor();
            if (cursor.moveToPosition(position)) {
                int shoppingListIdColIndex = cursor.getColumnIndex(ShoppingListContract._ID);
                int shoppingListNameColIndex = cursor.getColumnIndex(ShoppingListContract.COLUMN_LIST_NAME);
                long shoppingListId = cursor.getLong(shoppingListIdColIndex);
                String shoppingListName = cursor.getString(shoppingListNameColIndex);
                startShoppingListActivity(shoppingListName, shoppingListId);
            }
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> mShoppingListCursorLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri uri = ShoppingListContract.CONTENT_URI;

            String[] projection = {
                    ShoppingListContract._ID,
                    ShoppingListContract.COLUMN_LIST_NAME
            };

            String sortOrder = ShoppingListContract.COLUMN_LIST_NAME + " ASC";

            String selection = ShoppingListContract.COLUMN_USER_ID + "=?";
            String[] selectionArgs = {
                    mUserId
            };

            String shoppingListName = mEdtxtShoppingListName.getText().toString().trim();

            if (id == LOADER_SHOPPING_LIST_BY_USER_AND_NAME && !shoppingListName.equals("")) {
                selection += " AND " + ShoppingListContract.COLUMN_LIST_NAME + " LIKE ?";
                selectionArgs = new String[]{
                        mUserId,
                        shoppingListName + "%"
                };
            }

            return new CursorLoader(ShoppingListsPerUserActivity.this,
                    uri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mShoppingListAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mShoppingListAdapter.swapCursor(null);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shopping_lists_per_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                LocalCacheUtil.clearLastActivity(this);
                LocalCacheUtil.clearLastUser(this);
                mSaveActivityAsLast = false;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startShoppingListActivity(String shoppingListName, long shoppingListId) {
        String keyShoppingListName = getString(R.string.extra_key_shopping_list_name);
        String keyShoppingListId = getString(R.string.extra_key_shopping_list_id);

        Intent intent = new Intent(this, ShoppingListActivity.class);
        intent.putExtra(keyShoppingListName, shoppingListName);
        intent.putExtra(keyShoppingListId, shoppingListId);

        startActivity(intent);
    }
}
