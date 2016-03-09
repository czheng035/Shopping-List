package com.knowroaming.czheng035.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AllUserActivity extends AppCompatActivity {

    EditText mEdtxtNewUserId;
    Button mBtnNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        init();
    }

    private void init() {
        mEdtxtNewUserId = (EditText) findViewById(R.id.edtxt_user_id);
        mBtnNewUser = (Button) findViewById(R.id.btn_new_user);

        mBtnNewUser.setOnClickListener(mBtnNewUserOnClickListener);
    }

    private View.OnClickListener mBtnNewUserOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AllUserActivity.this, ShoppingListsPerUserActivity.class);
            startActivity(intent);
        }
    };
}
