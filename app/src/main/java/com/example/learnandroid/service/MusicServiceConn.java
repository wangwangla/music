package com.example.learnandroid.service;


import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.learnandroid.constant.MusicManager;

public class MusicServiceConn implements ServiceConnection { //用于实现连接服务
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicManager.musicController = (MusicControl) service;
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {
        MusicManager.musicController = null;
    }
}