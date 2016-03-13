package com.knowroaming.czheng035.shoppinglist.adapter.backup;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.knowroaming.czheng035.shoppinglist.data.ItemContract;

public class ItemAdapter extends CursorAdapter {

    public ItemAdapter(Context context, Cursor c, int flags) {
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
        int itemNameColumnIndex = cursor.getColumnIndex(ItemContract.COLUMN_ITEM_NAME);
        int itemStatusColumnIndex = cursor.getColumnIndex(ItemContract.COLUMN_ITEM_STATUS);
        int itemStatus = cursor.getInt(itemStatusColumnIndex);
        if(itemStatus == ItemContract.ITEM_DONE){
            viewHolder
                    .itemName
                    .setPaintFlags(viewHolder.itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder
                    .itemName
                    .setPaintFlags(viewHolder.itemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        viewHolder.itemName.setText(cursor.getString(itemNameColumnIndex));
    }

    public static class ViewHolder {
        public final TextView itemName;

        public ViewHolder(View view) {
            itemName = (TextView) view.findViewById(android.R.id.text1);
        }
    }
}
