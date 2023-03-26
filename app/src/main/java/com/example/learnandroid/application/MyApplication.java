package com.example.learnandroid.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IBinder;

import com.example.learnandroid.CustomTitleActivity;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.data.SongLoader;
import com.example.learnandroid.service.MusicControl;
import com.example.learnandroid.service.MusicService;
import com.example.learnandroid.sqlite.SqliteUtils;

import java.util.logging.Handler;

public class MyApplication extends Application {
    private static MyApplication musicContent;
    private MyServiceConn conn;

    @Override
    public void onCreate() {
        super.onCreate();
        musicContent = this;
        SongLoader.destory();
        SongLoader.setLoadType(0);
        SqliteUtils instance = (SqliteUtils) SqliteUtils.getInstance(this);
        Intent intent = new Intent(this, MusicService.class);//创建意图对象
        //创建服务连接对象
        conn = new MyServiceConn();
        bindService(intent, conn, BIND_AUTO_CREATE);  //绑定服务
    }

    public static MyApplication getMusicContent() {
        return musicContent;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        unbindService(conn);
        musicContent = null;
    }

    class MyServiceConn implements ServiceConnection { //用于实现连接服务
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicManager.musicController = (MusicControl) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }
}
