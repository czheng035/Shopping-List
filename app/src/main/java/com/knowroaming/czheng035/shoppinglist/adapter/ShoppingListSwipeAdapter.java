package com.knowroaming.czheng035.shoppinglist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.CursorSwipeAdapter;
import com.knowroaming.czheng035.shoppinglist.R;
import com.knowroaming.czheng035.shoppinglist.data.ShoppingListContract;

/**
 * This is a swipe layout adapter for the listview showing a specific user's
 * all saved shopping lists
 */
public class ShoppingListSwipeAdapter extends CursorSwipeAdapter {
    public ShoppingListSwipeAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.swipe_list_item_shopping_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int shoppingListIdColumnIndex = cursor.getColumnIndex(ShoppingListContract._ID);
        int shoppingListNameColumnIndex = cursor.getColumnIndex(ShoppingListContract.COLUMN_LIST_NAME);

        final long shoppingListId = cursor.getLong(shoppingListIdColumnIndex);

        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // The async task remove the selected shopping list
                AsyncTask<Void, Void, Void> mRemoveShoppingListTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Uri uri = ShoppingListContract.CONTENT_URI;

                        String selection = ShoppingListContract._ID + "=?";
                        String[] selectionArgs = {
                                Long.toString(shoppingListId)
                        };

                        context.getContentResolver().delete(
                                uri,
                                selection,
                                selectionArgs
                        );

                        return null;
                    }
                };

                mRemoveShoppingListTask.execute();
                closeAllItems();
            }
        });

        viewHolder.tvShoppingListName.setText(cursor.getString(shoppingListNameColumnIndex));
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_list_item_shopping_list;
    }

    @Override
    public void notifyDatasetChanged() {

    }

    private static class ViewHolder {
        public final SwipeLayout swipeLayout;

        public final TextView tvShoppingListName;
        public final Button btnRemove;

        public ViewHolder(View view) {
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_list_item_shopping_list);

            tvShoppingListName = (TextView) view.findViewById(android.R.id.text1);
            btnRemove = (Button) view.findViewById(R.id.btn_remove);
        }
    }
}
