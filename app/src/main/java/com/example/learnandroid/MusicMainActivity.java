package com.example.learnandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.broad.BroadUtils;
import com.example.learnandroid.constant.Constant;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.adapter.SectionsPagerAdapter;
import com.example.learnandroid.dialog.AboutFragmentDialog;
import com.example.learnandroid.notification.TimberUtils;
import com.example.learnandroid.service.MusicService;
import com.example.learnandroid.session.SessionUtils;
import com.example.learnandroid.application.utils.BitmapUtils;
import com.example.learnandroid.application.utils.TimeUtils;
import com.example.learnandroid.application.utils.VersionUtils;
import com.google.android.material.tabs.TabLayout;

import kw.learn.mylibrary.activity.BaseActivity;

public class MusicMainActivity extends BaseActivity {
    private boolean isBottomListener;
    private NotificationManager notificationManager;
    private SessionUtils sessionUtils;

    private Runnable quickPlayPanel = new Runnable() {
        @Override
        public void run() {
            updateBottomView();
        }
    };

    private Runnable processRunnable = new Runnable() {
        @Override
        public void run() {
            upateDateProcess();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        bottomClickListener();
        isBottomListener = true;
        updateBottomPanelData();
        initViewPager();
        MusicManager.addUpdateView(quickPlayPanel);
        MusicManager.addTimeView(processRunnable);
        initSearch();
        viewInsetArea();
    }


    @Override
    protected int getResourceId() {
        return R.layout.activity_custom_title;
    }

    private void initSearch() {
        View search = findViewById(R.id.btnSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicMainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            //自己写了标题，不在展示默认标题
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void updateBottomPanelData() {
        BroadUtils broadUtils = new BroadUtils();
        broadUtils.setFilter(this,new String[]{Constant.UP_DATE_BOTTOM,Constant.MUSIC_PRE});
    }

    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new SectionsPagerAdapter(this, getSupportFragmentManager()));
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    public void upateDateProcess(){
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressBar progressBar = findViewById(R.id.bottom_play_process);
                    progressBar.setProgress(TimeUtils.miao(MusicManager.getCurrentPosition()));
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initSession() {

    }

    public void updateBottomView(){
        MusicBean musicBean = MusicManager.getMusicBean();
        if (musicBean == null){
            return;
        }else {
            ImageView bottomSongPic = findViewById(R.id.bottom_song_pic);
            TextView bottomSongName = findViewById(R.id.bottom_song_name);
            TextView bottomSongSonger = findViewById(R.id.bottom_song_songer);
            ProgressBar bottomProcess = findViewById(R.id.bottom_play_process);
            Uri albumArtUri = BitmapUtils.getAlbumArtUri(musicBean.getAlbumId());
            Bitmap bitmap = BitmapUtils.decodeUri(MusicMainActivity.this.getBaseContext(),albumArtUri,300,300);
            if (bitmap!=null) {
                bottomSongPic.setImageBitmap(bitmap);
            }
            bottomProcess.setMax(TimeUtils.miao(musicBean.getDuration()));
            bottomSongName.setText(musicBean.getTitle());
            bottomSongSonger.setText(musicBean.getArtistName());
            MusicManager.setData();
            ImageView bottomSongPlayOrStop = findViewById(R.id.bottom_song_playorstop);
            if (MusicManager.isPlaying()) {
                bottomSongPlayOrStop.setImageResource(R.mipmap.pause);
            }else {
                bottomSongPlayOrStop.setImageResource(R.mipmap.play);
            }
        }
        MusicService.refreshMediaSessionState();
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
                Intent intent = new Intent(MusicMainActivity.this,PlayActivity.class);
                startActivity(intent);
            }
        });
    }

    private void viewInsetArea() {
        viewInset(findViewById(R.id.main_root), new OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                ViewCompat.onApplyWindowInsets(v, insets);
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_about){
            AboutFragmentDialog.show(getSupportFragmentManager());
        }else if (item.getItemId() == R.id.action_setting){
            Intent intent = new Intent(MusicMainActivity.this,SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}