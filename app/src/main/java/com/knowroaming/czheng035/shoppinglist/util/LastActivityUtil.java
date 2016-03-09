package com.knowroaming.czheng035.shoppinglist.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.knowroaming.czheng035.shoppinglist.AllUserActivity;
import com.knowroaming.czheng035.shoppinglist.R;

public class LastActivityUtil {

    private static String PREF_KEY_LAST_ACTIVITY = "last_activity";

    public static void saveLastActivity(Context context, Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_LAST_ACTIVITY, activity.getClass().getName());
        editor.commit();
    }

    public static void loadLastActivity(Context context) {
        Intent intent;

        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            intent = new Intent(context,
                    Class.forName(prefs.getString(PREF_KEY_LAST_ACTIVITY, "")));
        } catch(ClassNotFoundException e) {
            Log.i(LastActivityUtil.class.getName(),
                    context.getString(R.string.log_info_last_activity_not_found));
            intent = new Intent(context, AllUserActivity.class);
        }

        context.startActivity(intent);
    }
}
