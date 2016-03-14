package com.knowroaming.czheng035.shoppinglist.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class CommonUtil {
    public static void hideSoftKeyboard(View view, Context context) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean validateUserInput(String input) {
        if (input == null || input.trim().equals("")) return false;
        else return true;
    }
}
