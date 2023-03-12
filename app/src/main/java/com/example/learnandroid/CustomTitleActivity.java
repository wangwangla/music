package com.example.learnandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.Constant;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.adapter.SectionsPagerAdapter;
import com.example.learnandroid.service.MusicControl;
import com.example.learnandroid.service.MusicService;
import com.example.learnandroid.utils.MusicUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.Timer;
import java.util.TimerTask;

public class CustomTitleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_title);

        Intent intent = new Intent(this, MusicService.class);//创建意图对象
        //创建服务连接对象
        bindService(intent, new MyServiceConn(), BIND_AUTO_CREATE);  //绑定服务
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        sectionsPagerAdapter.setHandler(mainHandler);
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



//        ImageView playOrStop = findViewById(R.id.bottom_song_playorstop);
//        playOrStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!MusicManager.musicControl.isPlaying()) {
//                    if (MusicManager.musicControl.isCanPlay()) {
//                        MusicManager.musicControl.continueMusic();
//                        playOrStop.setImageResource(R.drawable.pause);
//                    }
//                }else {
//                    if (MusicManager.musicControl.isCanPlay()) {
//                        MusicManager.musicControl.pausePlay();
//                        playOrStop.setImageResource(R.drawable.play);
//                    }
//                }
//            }
//        });
//
//        View songNext = findViewById(R.id.bottom_song_next);
//        songNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MusicManager.playNext();
//            }
//        });
//        findViewById(R.id.bottom_play_view).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(CustomTitleActivity.this, PlayActivity.class);
//                startActivity(intent);
//            }
//        });

//        View bottomPlayView = findViewById(R.id.bottom_play_view);
//        bottomPlayView.post(new Runnable() {
//            @Override
//            public void run() {
//                int height= bottomPlayView.getHeight();
//                int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;//屏幕高度
//                Resources resources = getResources();
//                int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
//                int bottomStatusHeight = resources.getDimensionPixelSize(resourceId);
////                bottomPlayView.setY(0);
////                TranslateAnimation animation = new TranslateAnimation(
////                        0,0,
////                        screenHeight-bottomStatusHeight,
////                        screenHeight-height-bottomStatusHeight
////                );
////                animation.setFillAfter(true);
////                animation.setDuration(1000);
////                bottomPlayView.startAnimation(animation);
//            }
//        });
    }

    private void pause() {
        ImageView playOrStop = findViewById(R.id.bottom_song_playorstop);
        if (!MusicManager.musicController.isPlaying()) {
            playOrStop.setImageResource(R.drawable.play);
        }else {
            playOrStop.setImageResource(R.drawable.pause);
        }
    }

    class MyServiceConn implements ServiceConnection { //用于实现连接服务
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicManager.musicController = (MusicControl) service;
            Message message = new Message();
            message.what = Constant.INITBOTTOMVIEW;
            mainHandler.sendMessage(message);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constant.INITBOTTOMVIEW){
                updateBottomView();
            }
        }
    };

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

}