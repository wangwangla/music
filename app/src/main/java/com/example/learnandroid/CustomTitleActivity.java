package com.example.learnandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.learnandroid.application.MyApplication;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.broadcast.MyBroadcast;
import com.example.learnandroid.constant.Constant;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.adapter.SectionsPagerAdapter;
import com.example.learnandroid.notification.MusicNotification;
import com.example.learnandroid.service.MusicControl;
import com.example.learnandroid.service.MusicService;
import com.example.learnandroid.utils.MusicUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.Timer;
import java.util.TimerTask;

public class CustomTitleActivity extends AppCompatActivity {
    private MyServiceConn conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_title);

        Intent intent = new Intent(this, MusicService.class);//创建意图对象
        //创建服务连接对象
         conn = new MyServiceConn();
        bindService(intent, conn, BIND_AUTO_CREATE);  //绑定服务

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        bottomClickListener();
        MusicManager.addUpdateView(new Runnable() {
            @Override
            public void run() {
                updateBottomView();
            }
        });
    }

    class MyServiceConn implements ServiceConnection { //用于实现连接服务
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicManager.musicController = (MusicControl) service;
            updateBottomView();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void updateBottomView(){
        MusicBean musicBean = MusicManager.getMusicBean();
        if (musicBean == null){
            return;
        }else {
            ImageView bottomSongPic = findViewById(R.id.bottom_song_pic);
            TextView bottomSongName = findViewById(R.id.bottom_song_name);
            TextView bottomSongSonger = findViewById(R.id.bottom_song_songer);
            Uri albumArtUri = MusicUtils.getAlbumArtUri(musicBean.getAlbumId());
            Bitmap bitmap = MusicUtils.decodeUri(CustomTitleActivity.this.getBaseContext(),albumArtUri,300,300);
            if (bitmap!=null) {
                bottomSongPic.setImageBitmap(bitmap);
            }
            bottomSongName.setText(musicBean.getTitle());
            bottomSongSonger.setText(musicBean.getArtistName());
            MusicManager.setData();
            ImageView bottomSongPlayOrStop = findViewById(R.id.bottom_song_playorstop);
            if (MusicManager.isPlaying()) {
                bottomSongPlayOrStop.setImageResource(R.drawable.pause);
            }else {
                bottomSongPlayOrStop.setImageResource(R.drawable.play);
            }
        }
        buildNotification(musicBean);
    }

    private void buildNotification(MusicBean musicBean) {
        int res = R.drawable.ic_play_white_36dp;
        if (MusicManager.isPlaying()) {
            res = R.drawable.ic_pause_white_36dp;
        }
        Uri albumArtUri = MusicUtils.getAlbumArtUri(musicBean.getAlbumId());
        Bitmap bitmap = MusicUtils.decodeUri(CustomTitleActivity.this.getBaseContext(),albumArtUri,300,300);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getMusicContent(), "0")
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(bitmap)
                .setContentTitle(musicBean.getTitle())
                .setContentText(musicBean.getArtistName())
                .setWhen(0)
                .addAction(R.drawable.ic_skip_previous_white_36dp,
                        "",retrievePlaybackAction(Constant.MUSIC_PRE)).
                addAction(res,
                        "",retrievePlaybackAction(Constant.MUSIC_STOP)).
                addAction(R.drawable.ic_skip_next_white_36dp,
                        "",retrievePlaybackAction(Constant.MUSIC_NEXT));

        notificationManager.notify(0, builder.build());
    }


    private final PendingIntent retrievePlaybackAction(final String action) {
        final ComponentName serviceName = new ComponentName(this, MusicService.class);
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }



    private void bottomClickListener() {
        ImageView bottomSongNext = findViewById(R.id.bottom_song_next);
        ImageView bottomSongPlayOrStop = findViewById(R.id.bottom_song_playorstop);

        bottomSongNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playNext();
            }
        });
        bottomSongPlayOrStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicManager.isPlaying()) {
                    MusicManager.pausePlay();
                }else {
                    MusicManager.continuePlay();
                }
            }
        });

        View bottomPlayView = findViewById(R.id.bottom_play_view);
        bottomPlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomTitleActivity.this,PlayActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}