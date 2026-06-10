package com.example.learnandroid.service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.example.learnandroid.constant.Constant;
import com.example.learnandroid.constant.MusicManager;

import java.util.List;

public class MusicService extends MediaBrowserServiceCompat {
    //添加日志的TAG常量
    private MediaPlayer player;

    public MusicService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();//创建音乐播放器对象
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        if (intent == null || intent.getAction() == null) {
            return START_NOT_STICKY;
        }
        boolean shouldRefreshUi = false;
        String action = intent.getAction();
        if (Constant.MUSIC_PRE.equals(action)) {
            MusicManager.playPre();
            shouldRefreshUi = true;
        } else if (Constant.MUSIC_NEXT.equals(action)) {
            MusicManager.playNext();
            shouldRefreshUi = true;
        } else if (Constant.MUSIC_STOP.equals(action)) {
            if (MusicManager.isPlaying()) {
                MusicManager.pausePlay();
            } else {
                MusicManager.continuePlay();
            }
            shouldRefreshUi = true;
        }
        if (shouldRefreshUi) {
            sendBroadcast(new Intent(Constant.UP_DATE_BOTTOM));
        }
        return START_NOT_STICKY;
    }

        @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl(player);
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
//        return null;
        return new BrowserRoot(clientPackageName, null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

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
