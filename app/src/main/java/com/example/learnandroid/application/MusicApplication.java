package com.example.learnandroid.application;

import android.app.Application;
import android.content.Intent;
import com.example.learnandroid.data.SongLoader;
import com.example.learnandroid.service.MusicService;
import com.example.learnandroid.service.MusicServiceConn;

public class MusicApplication extends Application {
    private static MusicApplication instance;
    private MusicServiceConn musicServiceConn;

    @Override
    public void onCreate() {
        super.onCreate();
         instance= this;
        SongLoader.destory();
        //加载音乐使用的方法
        SongLoader.setLoadType(0);
        bingService();
    }

    private void bingService() {
        //创建音乐的服务
        Intent intent = new Intent(this, MusicService.class);//创建意图对象
        //创建服务连接对象
        musicServiceConn = new MusicServiceConn();
        bindService(intent, musicServiceConn, BIND_AUTO_CREATE);  //绑定服务
    }

    public static MusicApplication getMusicContent() {
        return instance;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (musicServiceConn!=null) {
            unbindService(musicServiceConn);
            musicServiceConn = null;
        }
        instance = null;
    }
}