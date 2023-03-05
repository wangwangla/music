package com.example.learnandroid.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
    //添加日志的TAG常量
    private MediaPlayer player;

    public MusicService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl(getApplicationContext(),player);
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
