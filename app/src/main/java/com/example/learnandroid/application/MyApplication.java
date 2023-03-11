package com.example.learnandroid.application;

import android.app.Application;

import com.example.learnandroid.data.SongLoader;

import java.util.logging.Handler;

public class MyApplication extends Application {
    private static MyApplication musicContent;

    @Override
    public void onCreate() {
        super.onCreate();
        musicContent = this;
        SongLoader.destory();
        SongLoader.setLoadType(0);
    }

    public static MyApplication getMusicContent() {
        return musicContent;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        musicContent = null;
    }
}
