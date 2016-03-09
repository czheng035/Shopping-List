package com.knowroaming.czheng035.shoppinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.knowroaming.czheng035.shoppinglist.util.LastActivityUtil;

public class ShoppingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LastActivityUtil.saveLastActivity(this, this);
    }
}
