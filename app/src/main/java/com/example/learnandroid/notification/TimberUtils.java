package com.example.learnandroid.notification;

import android.os.Build;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/26 17:06
 */
public class TimberUtils {

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
