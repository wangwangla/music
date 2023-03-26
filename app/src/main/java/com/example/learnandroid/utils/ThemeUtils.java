package com.example.learnandroid.utils;

import static android.icu.lang.UCharacter.JoiningType.TRANSPARENT;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import com.example.learnandroid.application.MyApplication;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/26 7:23
 */
public class ThemeUtils {
    //设置背景
    public static void updateSystemBarContent(Activity activity,boolean isStatus){
        final View decorView = activity.getWindow().getDecorView();
        final int systemUiVisibility = decorView.getSystemUiVisibility();
        if (isStatus) {
//        黑
            decorView.setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }else {
//        白
            decorView.setSystemUiVisibility(systemUiVisibility & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public static void hideTopBg(Activity activity){
        final View decorView = activity.getWindow().getDecorView();
        //状态栏效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView1 = activity.getWindow().getDecorView();
            //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            //Activity全屏显示，但是状态栏不会被覆盖掉，而是正常显示
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //设置状态栏背景颜色为透明
            activity.getWindow().setStatusBarColor(TRANSPARENT);
            //设置状态栏字体颜色为深色
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }
}
