package com.knowroaming.czheng035.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.knowroaming.czheng035.shoppinglist.util.LastActivityUtil;

public class ShoppingListsPerUserActivity extends AppCompatActivity {

    private EditText mEdtxtShoppingListName;
    private Button mBtnNewShoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_lists_per_user);

        init();
    }

    @Override
    protected void onPause() {
        super.onPause();

        LastActivityUtil.saveLastActivity(this, this);
    }

    private void init() {
        mEdtxtShoppingListName = (EditText) findViewById(R.id.edtxt_shopping_list_name);
        mBtnNewShoppingList = (Button) findViewById(R.id.btn_new_shopping_list);

        mBtnNewShoppingList.setOnClickListener(mBtnNewShoppingListOnClickListener);
    }

    private View.OnClickListener mBtnNewShoppingListOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ShoppingListsPerUserActivity.this, ShoppingListActivity.class);
            startActivity(intent);
        }
    };
}
