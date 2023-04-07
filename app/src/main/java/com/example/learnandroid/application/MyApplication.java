package com.example.learnandroid.application;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.learnandroid.CustomTitleActivity;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.data.SongLoader;
import com.example.learnandroid.service.MusicControl;
import com.example.learnandroid.service.MusicService;
import com.example.learnandroid.sqlite.SqliteUtils;

import java.util.logging.Handler;

public class MyApplication extends Application {
    private static MyApplication instance;
    private MyServiceConn conn;

    @Override
    public void onCreate() {
        super.onCreate();
         instance= this;
        SongLoader.destory();
        //加载音乐使用的方法
        SongLoader.setLoadType(0);
        SqliteUtils instance = (SqliteUtils) SqliteUtils.getInstance(this);
        //创建音乐的服务
        Intent intent = new Intent(this, MusicService.class);//创建意图对象
        //创建服务连接对象
        conn = new MyServiceConn();
        bindService(intent, conn, BIND_AUTO_CREATE);  //绑定服务
    }

    public static MyApplication getMusicContent() {
        return instance;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        unbindService(conn);
        instance = null;
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
