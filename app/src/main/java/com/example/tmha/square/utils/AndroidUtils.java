package com.example.tmha.square.utils;

import android.os.Build;

/**
 * Created by tmha on 6/23/2017.
 */

public class AndroidUtils {
    public static boolean isLollipop() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
