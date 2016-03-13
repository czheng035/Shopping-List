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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.knowroaming.czheng035.shoppinglist.adapter.UserAdapter;
import com.knowroaming.czheng035.shoppinglist.data.UserContract;
import com.knowroaming.czheng035.shoppinglist.util.LocalCacheUtil;

public class MainActivity extends AppCompatActivity {

    private final static int LOADER_USER = 0;
    private final static int LOADER_USER_BY_USER_ID = 1;

    private EditText mEdtxtUserId;
    private Button mBtnNewUser;
    private ListView mUserListView;

    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent lastActivityIntent = LocalCacheUtil.getLastActivityIntent(this);
        if (lastActivityIntent != null) {
            restoreLastActivity(lastActivityIntent);
        }

        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mEdtxtUserId = (EditText) findViewById(R.id.edtxt_user_id);
        mBtnNewUser = (Button) findViewById(R.id.btn_new_user);
        mUserListView = (ListView) findViewById(R.id.user_list);

        mEdtxtUserId.addTextChangedListener(mEdtxtUserIdTextWatcher);
        mBtnNewUser.setOnClickListener(mBtnNewUserOnClickListener);

        mUserAdapter = new UserAdapter(this, null, 0);
        mUserListView.setAdapter(mUserAdapter);
        mUserListView.setOnItemClickListener(mUserListViewOnItemClickListener);

        getSupportLoaderManager().initLoader(LOADER_USER, null, mUserCursorLoader);
    }

    private void restoreLastActivity(Intent intent) {
        String userId = LocalCacheUtil.getLastUser(this);
        long shoppingListId = LocalCacheUtil.getLastShoppingListId(this);
        String shoppinglistName = LocalCacheUtil.getLastShoppingListName(this);

        if (userId != null) {
            intent.putExtra(getString(R.string.extra_key_user_id), userId);
        }

        if (shoppingListId != 0) {
            intent.putExtra(getString(R.string.extra_key_shopping_list_id), shoppingListId);
        }

        if (shoppinglistName != null) {
            intent.putExtra(getString(R.string.extra_key_shopping_list_name), shoppinglistName);
        }

        startActivity(intent);
    }

    private TextWatcher mEdtxtUserIdTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            getSupportLoaderManager().restartLoader(LOADER_USER_BY_USER_ID, null, mUserCursorLoader);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private View.OnClickListener mBtnNewUserOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String userId = mEdtxtUserId.getText().toString().trim();
            if (validateUserId(userId)) {
                AsyncTask<String, Void, Boolean> addNewUserAsyncTask
                        = new AsyncTask<String, Void, Boolean>() {
                    private String mUserId;

                    @Override
                    protected Boolean doInBackground(String... params) {
                        mUserId = params[0];

                        Uri uri = UserContract.CONTENT_URI;
                        String selection = UserContract.COLUMN_USER_ID + "=?";
                        String[] selectionArgs = {
                                mUserId
                        };

                        String sortOrder = UserContract.COLUMN_USER_ID + " ASC";

                        Cursor cursor = getContentResolver().query(
                                uri,
                                null,
                                selection,
                                selectionArgs,
                                sortOrder
                        );

                        if (cursor.getCount() != 0) return false;

                        ContentValues userValues = new ContentValues();
                        userValues.put(UserContract.COLUMN_USER_ID, mUserId);

                        getContentResolver().insert(uri, userValues);

                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if (result) {
                            startShoppingListsPerUserActivity(mUserId);
                        } else {
                            Snackbar.make(findViewById(R.id.root_layout),
                                    R.string.msg_user_id_exist, Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                };

                addNewUserAsyncTask.execute(userId);
            }
            mEdtxtUserId.setText(null);
        }
    };

    private boolean validateUserId(String userId) {
        if (userId == null || userId.trim().equals("")) return false;
        else return true;
    }

    private AdapterView.OnItemClickListener mUserListViewOnItemClickListener
            = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = mUserAdapter.getCursor();
            if (cursor.moveToPosition(position)) {
                int userIdColIndex = cursor.getColumnIndex(UserContract.COLUMN_USER_ID);
                String userId = cursor.getString(userIdColIndex);
                startShoppingListsPerUserActivity(userId);
            }
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> mUserCursorLoader
            = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            Uri uri = UserContract.CONTENT_URI;

            String[] projection = {
                    UserContract._ID,
                    UserContract.COLUMN_USER_ID
            };

            String sortOrder = UserContract.COLUMN_USER_ID + " ASC";

            String selection = null;
            String[] selectionArgs = null;

            String userId = mEdtxtUserId.getText().toString().trim();

            if (id == LOADER_USER_BY_USER_ID && !userId.equals("")) {
                selection = UserContract.COLUMN_USER_ID + " LIKE ?";
                selectionArgs = new String[]{
                        userId + "%"
                };
            }

            return new CursorLoader(MainActivity.this,
                    uri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mUserAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mUserAdapter.swapCursor(null);
        }
    };

    private void startShoppingListsPerUserActivity(String userId) {
        String keyUserId = getString(R.string.extra_key_user_id);

        Intent intent = new Intent(this, ShoppingListsPerUserActivity.class);
        intent.putExtra(keyUserId, userId);

        startActivity(intent);
    }
}
