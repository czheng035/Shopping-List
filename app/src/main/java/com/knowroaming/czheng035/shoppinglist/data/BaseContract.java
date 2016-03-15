package com.knowroaming.czheng035.shoppinglist.data;

import android.net.Uri;

/**
 * This is the base contract interface with common constants like application content authority
 * and base content uri
 */
public interface BaseContract {

    public static final String CONTENT_AUTHORITY = "com.knowroaming.czheng035.shoppinglist";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
}
