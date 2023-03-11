package com.example.learnandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.learnandroid.application.MyApplication;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/11 17:13
 */
public class SharePerenceUtils {
    private SharedPreferences sharedPreferences;
    private static SharePerenceUtils sharePerenceUtils;
    public SharePerenceUtils(){
        destoy();
        sharedPreferences = MyApplication.getMusicContent().getSharedPreferences("test", Context.MODE_PRIVATE);
    }

    public static SharePerenceUtils getSharePerenceUtils() {
        if (sharePerenceUtils == null){
             sharePerenceUtils = new SharePerenceUtils();
        }
        return sharePerenceUtils;
    }

    public void destoy(){
        sharePerenceUtils = null;
    }

    public int histotyPosition() {
        return sharedPreferences.getInt("history_position",0);
    }
}
