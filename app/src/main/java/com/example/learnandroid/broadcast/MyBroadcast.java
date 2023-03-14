package com.example.learnandroid.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.learnandroid.constant.Constant;
import com.example.learnandroid.constant.MusicManager;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/13 23:02
 */
public class MyBroadcast extends BroadcastReceiver {
    @Override
    // 当广播接收者接收广播成功，此方法会自动回调执行，在该方法中实现广播接收者的相关操作
    public void onReceive(Context context, Intent intent) {
        String value = intent.getStringExtra(Constant.MUSIC_KEY);
        System.out.println("------------------"+value);
        if (value.equals(Constant.MUSIC_PRE)) {
            MusicManager.playPre();
        }else if (value.equals(Constant.MUSIC_NEXT)){
            MusicManager.playNext();
        }else if (value.equals(Constant.MUSIC_STOP)){
            if (MusicManager.isPlaying()) {
                MusicManager.pausePlay();
            }else {
                MusicManager.continuePlay();
            }
        }
    }
}