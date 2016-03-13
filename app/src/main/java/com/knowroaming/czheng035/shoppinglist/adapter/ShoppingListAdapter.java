package com.knowroaming.czheng035.shoppinglist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.knowroaming.czheng035.shoppinglist.data.ShoppingListContract;

public class ShoppingListAdapter extends CursorAdapter {
    public ShoppingListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_1, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int shoppingListNameColumnIndex = cursor.getColumnIndex(ShoppingListContract.COLUMN_LIST_NAME);
        viewHolder.shoppingListName.setText(cursor.getString(shoppingListNameColumnIndex));
    }

    public static class ViewHolder {
        public final TextView shoppingListName;

        public ViewHolder(View view) {
            shoppingListName = (TextView) view.findViewById(android.R.id.text1);
        }
    }
}
