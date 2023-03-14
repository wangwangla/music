package com.example.learnandroid.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.learnandroid.LoadingActivity;
import com.example.learnandroid.PlayActivity;
import com.example.learnandroid.R;
import com.example.learnandroid.application.MyApplication;
import com.example.learnandroid.constant.Constant;

public class MusicService extends Service {
    //添加日志的TAG常量
    private MediaPlayer player;

    public MusicService() {

    }



    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        String action = intent.getAction();
        String musicValue = intent.getStringExtra(Constant.MUSIC_KEY);
        Intent inten = new Intent(Constant.MUSIC_TYPE);
        inten.putExtra(Constant.MUSIC_KEY,musicValue);
        sendBroadcast(inten);
        return START_NOT_STICKY;
    }

        @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl(player);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();//创建音乐播放器对象
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player == null) return;
        if (player.isPlaying()) player.stop();//停止播放音乐
        player.release();                         //释放占用的资源
        player = null;                            //将player置为空
    }


}
