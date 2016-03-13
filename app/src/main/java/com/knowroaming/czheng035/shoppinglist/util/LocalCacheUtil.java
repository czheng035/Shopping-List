package com.knowroaming.czheng035.shoppinglist.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.knowroaming.czheng035.shoppinglist.R;

public class LocalCacheUtil {

    private static String PREF_KEY_LAST_ACTIVITY = "last_activity";
    private static String PREF_KEY_LAST_USER = "last_user";
    private static String PREF_KEY_LAST_SHOPPING_LIST_ID = "last_shopping_list_id";
    private static String PREF_KEY_LAST_SHOPPING_LIST_NAME = "last_shopping_list_name";

    public static void saveLastActivity(Context context, Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_LAST_ACTIVITY, activity.getClass().getName());
        editor.commit();
    }

    public static Intent getLastActivityIntent(Context context) {
        Intent intent = null;

        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            intent = new Intent(context,
                    Class.forName(prefs.getString(PREF_KEY_LAST_ACTIVITY, "")));
        } catch(ClassNotFoundException e) {
            Log.i(LocalCacheUtil.class.getName(),
                    context.getString(R.string.log_info_last_activity_not_found));
        }

        return intent;
    }

    public static void clearLastActivity(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PREF_KEY_LAST_ACTIVITY);
        editor.commit();
    }

    public static void saveLastUser(Context context, String userId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_LAST_USER, userId);
        editor.commit();
    }

    public static String getLastUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREF_KEY_LAST_USER, null);
    }

    public static void clearLastUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PREF_KEY_LAST_USER);
        editor.commit();
    }

    public static void saveLastShoppingList(Context context,
                                            String shoppingListName, long shoppingListId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREF_KEY_LAST_SHOPPING_LIST_ID, shoppingListId);
        editor.putString(PREF_KEY_LAST_SHOPPING_LIST_NAME, shoppingListName);
        editor.commit();
    }

    public static String getLastShoppingListName(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREF_KEY_LAST_SHOPPING_LIST_NAME, null);
    }

    public static long getLastShoppingListId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(PREF_KEY_LAST_SHOPPING_LIST_ID, 0);
    }

    public static void clearLastShoppingList(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PREF_KEY_LAST_SHOPPING_LIST_NAME);
        editor.commit();
    }
}
