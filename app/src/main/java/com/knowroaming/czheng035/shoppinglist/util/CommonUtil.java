package com.knowroaming.czheng035.shoppinglist.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class CommonUtil {

    /**
     * Hide soft keyboard for vew
     * @param view The view for which soft keyboard will be hidden
     * @param context The context invoking the method
     */
    public static void hideSoftKeyboard(View view, Context context) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Validate user input to make it not null or blank
     * @param input user input
     * @return true if it is a valid user input, false if not
     */
    public static boolean validateUserInput(String input) {
        if (input == null || input.trim().equals("")) return false;
        else return true;
    }
}
