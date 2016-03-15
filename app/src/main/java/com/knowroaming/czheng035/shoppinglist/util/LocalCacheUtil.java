package com.knowroaming.czheng035.shoppinglist.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.knowroaming.czheng035.shoppinglist.R;

/**
 * This class provides a set of utilities which facilitate storing and retrieving
 * last seen activity, last user, and last shopping list when users terminate the app
 */
public class LocalCacheUtil {

    private static String PREF_KEY_LAST_ACTIVITY = "last_activity";
    private static String PREF_KEY_LAST_USER = "last_user";
    private static String PREF_KEY_LAST_SHOPPING_LIST_ID = "last_shopping_list_id";
    private static String PREF_KEY_LAST_SHOPPING_LIST_NAME = "last_shopping_list_name";

    /**
     * Save the last seen activity
     * @param context The context invoking the method
     * @param activity The last seen activity to save
     */
    public static void saveLastActivity(Context context, Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_LAST_ACTIVITY, activity.getClass().getName());
        editor.commit();
    }

    /**
     * Get the intent for the last seen activity
     * @param context The context of the invoking activity
     * @return The intent for the last seen activity
     */
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

    /**
     * Clear the saved last seen activity in the preference file
     * @param context
     */
    public static void clearLastActivity(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PREF_KEY_LAST_ACTIVITY);
        editor.commit();
    }

    /**
     * Save the last user id
     * @param context The context invoking the method
     * @param userId The user id to save
     */
    public static void saveLastUser(Context context, String userId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_LAST_USER, userId);
        editor.commit();
    }

    /**
     * Get the last user id. Please note this is not the user._id, but user.user_id in the
     * user table
     * @param context The context invoking hte method
     * @return The last user id
     */
    public static String getLastUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREF_KEY_LAST_USER, null);
    }

    /**
     * Clear the saved last user
     * @param context The context invoking the method
     */
    public static void clearLastUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PREF_KEY_LAST_USER);
        editor.commit();
    }

    /**
     * Save the id and name of the last shopping list edited by user.
     * @param context The context invoking the method
     * @param shoppingListName The shopping list name
     * @param shoppingListId The shopping list id
     */
    public static void saveLastShoppingList(Context context,
                                            String shoppingListName, long shoppingListId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREF_KEY_LAST_SHOPPING_LIST_ID, shoppingListId);
        editor.putString(PREF_KEY_LAST_SHOPPING_LIST_NAME, shoppingListName);
        editor.commit();
    }

    /**
     * Retrieve the name of the saved last shopping list edited by user
     * @param context The context invoking the method
     * @return The last shopping list name
     */
    public static String getLastShoppingListName(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREF_KEY_LAST_SHOPPING_LIST_NAME, null);
    }

    /**
     * Retrieve the id of the saved last shopping list edited by user
     * @param context The context invoking the method
     * @return The last shopping list id
     */
    public static long getLastShoppingListId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(PREF_KEY_LAST_SHOPPING_LIST_ID, 0);
    }

    /**
     * Clear the last shopping list
     * @param context The context invoking the method
     */
    public static void clearLastShoppingList(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PREF_KEY_LAST_SHOPPING_LIST_NAME);
        editor.remove(PREF_KEY_LAST_SHOPPING_LIST_ID);
        editor.commit();
    }
}
