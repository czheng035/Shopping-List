package com.knowroaming.czheng035.shoppinglist.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
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
import com.knowroaming.czheng035.shoppinglist.data.ItemContract;

public class ItemSwipeAdapter extends CursorSwipeAdapter {
    public ItemSwipeAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.swipe_list_item_shopping_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int itemIdColumnIndex = cursor.getColumnIndex(ItemContract._ID);
        int itemNameColumnIndex = cursor.getColumnIndex(ItemContract.COLUMN_ITEM_NAME);
        int itemStatusColumnIndex = cursor.getColumnIndex(ItemContract.COLUMN_ITEM_STATUS);

        final long itemId = cursor.getLong(itemIdColumnIndex);

        int itemStatus = cursor.getInt(itemStatusColumnIndex);
        if(itemStatus == ItemContract.ITEM_DONE){
            viewHolder
                    .tvItemName
                    .setPaintFlags(viewHolder.tvItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.btnDone.setVisibility(View.GONE);
        } else {
            viewHolder
                    .tvItemName
                    .setPaintFlags(viewHolder.tvItemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.btnDone.setVisibility(View.VISIBLE);
            viewHolder.btnDone.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    AsyncTask<Void, Void, Void> mUpdateItemStatusTask = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            Uri uri = ItemContract.CONTENT_URI;

                            ContentValues itemValues = new ContentValues();
                            itemValues.put(ItemContract.COLUMN_ITEM_STATUS, ItemContract.ITEM_DONE);

                            String selection = ItemContract._ID + "=?";
                            String[] selectionArgs = {
                                    Long.toString(itemId)
                            };

                            context.getContentResolver().update(
                                    uri,
                                    itemValues,
                                    selection,
                                    selectionArgs
                            );

                            return null;
                        }
                    };

                    mUpdateItemStatusTask.execute();
                    closeAllItems();
                }
            });
        }

        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTask<Void, Void, Void> mRemoveItemTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Uri uri = ItemContract.CONTENT_URI;

                        String selection = ItemContract._ID + "=?";
                        String[] selectionArgs = {
                                Long.toString(itemId)
                        };

                        context.getContentResolver().delete(
                                uri,
                                selection,
                                selectionArgs
                        );

                        return null;
                    }
                };

                mRemoveItemTask.execute();
                closeAllItems();
            }
        });

        viewHolder.tvItemName.setText(cursor.getString(itemNameColumnIndex));
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_list_item_shopping_item;
    }

    @Override
    public void notifyDatasetChanged() {}

    private static class ViewHolder {
        public final SwipeLayout swipeLayout;

        public final TextView tvItemName;
        public final Button btnRemove;
        public final Button btnDone;

        public ViewHolder(View view) {
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_list_item_shopping_item);

            tvItemName = (TextView) view.findViewById(android.R.id.text1);
            btnRemove = (Button) view.findViewById(R.id.btn_remove);
            btnDone = (Button) view.findViewById(R.id.btn_done);
        }
    }
}
