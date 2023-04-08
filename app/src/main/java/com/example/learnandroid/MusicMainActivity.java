package com.example.learnandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.learnandroid.application.MyApplication;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.broadcast.MainBroadCast;
import com.example.learnandroid.constant.Constant;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.adapter.SectionsPagerAdapter;
import com.example.learnandroid.dialog.DialogUtils;
import com.example.learnandroid.notification.TimberUtils;
import com.example.learnandroid.service.MusicService;
import com.example.learnandroid.utils.BitmapUtils;
import com.example.learnandroid.utils.ThemeUtils;
import com.example.learnandroid.utils.TimeUtils;
import com.example.learnandroid.utils.VersionUtils;
import com.google.android.material.tabs.TabLayout;

public class MusicMainActivity extends AppCompatActivity {

    private MediaSessionCompat mSession;
    private boolean isBottomListener;

    private Runnable bottomStatus = new Runnable() {
        @Override
        public void run() {
            updateBottomView();
        }
    };

    private Runnable processRunnable = new Runnable() {
        @Override
        public void run() {
            upateDate();
        }
    };


    private MediaSessionCompat.Callback mediasessionBack = new MediaSessionCompat.Callback() {
        @Override
        public void onPause() {
//                pause();
//                mPausedByTransientLossOfFocus = false;
        }

        @Override
        public void onPlay() {
//                play();
        }

        @Override
        public void onSeekTo(long pos) {
//                seek(pos);
        }

        @Override
        public void onSkipToNext() {
//                gotoNext(true);
        }

        @Override
        public void onSkipToPrevious() {
//                prev(false);
        }

        @Override
        public void onStop() {
//                pause();
//                mPausedByTransientLossOfFocus = false;
//                seek(0);
//                releaseServiceUiAndStop();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_title);
        initSession();
        initToolbar();
        initStautarbar();
        updateBottomPanelData();
        initViewPager();
//        ColorFilter colorFilter = new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
//        toolbar.getOverflowIcon().setColorFilter(colorFilter);
        MusicManager.addUpdateView(bottomStatus);
        MusicManager.addTimeView(processRunnable);
        View search = findViewById(R.id.btnSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicMainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViewPager() {
        SectionsPagerAdapter sectionsPagerAdapter
                = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private void initStautarbar() {
        ThemeUtils.updateSystemBarContent(this,true);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void updateBottomPanelData() {
        MainBroadCast mainBroadCast = new MainBroadCast(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.UP_DATE_BOTTOM);
        registerReceiver(mainBroadCast,filter);
    }

    public void upateDate(){
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
        mSession = new MediaSessionCompat(this, "Timber");
        mSession.setCallback(mediasessionBack);
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        mSession.setActive(true);
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
                bottomSongPlayOrStop.setImageResource(R.drawable.pause);
            }else {
                bottomSongPlayOrStop.setImageResource(R.drawable.play);
            }
        }
        createNotificationChannel();
        buildNotification(musicBean);
        if (!isBottomListener) {
            isBottomListener = true;
            bottomClickListener();
        }
    }

    private void createNotificationChannel() {
        //大于26的需要通过通道来创建
        if (VersionUtils.isOreo()) {
            CharSequence name = "Timber";
            int importance = NotificationManager.IMPORTANCE_LOW;
            //得到manager
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //创建通道   id  名字  重要性
            NotificationChannel mChannel = new NotificationChannel("XXX", name, importance);
            manager.createNotificationChannel(mChannel);
        }
    }

    private void buildNotification(MusicBean musicBean) {
        int res = R.drawable.ic_play_white_36dp;
        if (MusicManager.isPlaying()) {
            res = R.drawable.ic_pause_white_36dp;
        }
        Uri albumArtUri = BitmapUtils.getAlbumArtUri(musicBean.getAlbumId());
        Bitmap bitmap = BitmapUtils.decodeUri(MusicMainActivity.this.getBaseContext(),albumArtUri,300,300);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getMusicContent(), "XXX")
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
        if (TimberUtils.isLollipop()) {
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
            androidx.media.app.NotificationCompat.MediaStyle style = new androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mSession.getSessionToken())
                    .setShowActionsInCompactView(0, 1, 2, 3)
                    ;
            builder.setStyle(style);
        }
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
                Intent intent = new Intent(MusicMainActivity.this,PlayActivity.class);
                startActivity(intent);
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
        switch (item.getItemId()){
            case R.id.action_about:
                DialogUtils.showNormalDialog(MusicMainActivity.this);
                break;
            case R.id.action_setting:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}