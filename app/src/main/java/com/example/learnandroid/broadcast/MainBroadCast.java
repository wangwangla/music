package com.example.learnandroid.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.learnandroid.MusicMainActivity;
import com.example.learnandroid.constant.Constant;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/18 18:00
 */
public class MainBroadCast extends BroadcastReceiver {
    private MusicMainActivity activity;
    public MainBroadCast(MusicMainActivity activity){
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Constant.UP_DATE_BOTTOM)) {
            activity.updateBottomView();
        }
    }
}
