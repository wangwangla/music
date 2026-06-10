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
        initSession();
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
//                    updateNotification();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateNotification() {
        if (!VersionUtils.isOreo())return;
        // 更新播放进度和播放状态
        PlaybackStateCompat playbackState
                = new PlaybackStateCompat
                .Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING,
                        MusicManager.getCurrentPosition(), 1.0f)
                .setBufferedPosition(MusicManager.getCurrentPosition())
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE
                        | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .build();
        sessionUtils.getmSession().setPlaybackState(playbackState);
//        // 更新播放进度条
        updatePlaybackProgress();
    }

    private void updatePlaybackProgress() {
        if (sessionUtils.getmSession() == null) {
            return;
        }
        MusicBean musicBean = MusicManager.getMusicBean();
        sessionUtils.getmSession().setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, musicBean.getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, musicBean.getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, musicBean.getAlbumName())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, musicBean.getTitle())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, MusicManager.getDuration())
//                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, getQueuePosition() + 1)
//                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getQueue().length)
//                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, getGenreName())
//                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                .build());
        // 获取当前播放位置和总时长
        long currentPosition = MusicManager.getCurrentPosition();
        long duration = MusicManager.getDuration();
        // 更新播放进度条
        PlaybackStateCompat playbackState = sessionUtils.getmSession().getController().getPlaybackState();
        if (playbackState != null && playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            if (duration > 0) {
                int progress = (int) (currentPosition * 100 / duration);
//                builder.setProgress(100, progress, false);
//                notificationManager.notify(0,builder.build());
            }
        }
    }

    private void initSession() {
        sessionUtils = new SessionUtils(this);
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

    private void createNotificationChannel() {
        //大于26的需要通过通道来创建
        if (VersionUtils.isOreo()) {
            CharSequence name = "Music";
            int importance = NotificationManager.IMPORTANCE_LOW;
            //得到manager
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //创建通道   id  名字  重要性
            NotificationChannel mChannel = new NotificationChannel("XXX", name, importance);
            manager.createNotificationChannel(mChannel);
        }
    }

    private PendingIntent retrievePlaybackAction(final String action,int code) {
        final ComponentName serviceName = new ComponentName(this, MusicService.class);
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);
        Log.d("MusicMainActivity", "retrievePlaybackAction: action="+action);
        return PendingIntent.getService(this, code, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
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