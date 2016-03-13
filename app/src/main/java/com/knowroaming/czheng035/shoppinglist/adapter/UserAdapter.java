package com.knowroaming.czheng035.shoppinglist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.knowroaming.czheng035.shoppinglist.data.UserContract;

public class UserAdapter extends CursorAdapter {
    public UserAdapter(Context context, Cursor c, int flags) {
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
        int userIdColumnIndex = cursor.getColumnIndex(UserContract.COLUMN_USER_ID);
        viewHolder.userId.setText(cursor.getString(userIdColumnIndex));
    }

    public static class ViewHolder {
        public final TextView userId;

        public ViewHolder(View view) {
            userId = (TextView) view.findViewById(android.R.id.text1);
        }
    }
}
