package com.knowroaming.czheng035.shoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.knowroaming.czheng035.shoppinglist.util.LastActivityUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        LastActivityUtil.loadLastActivity(this);
    }
}
