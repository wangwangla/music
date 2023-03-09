package com.example.learnandroid;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.Image;
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

import java.util.ArrayList;

public class CustomTitleActivity extends AppCompatActivity {
    private MyServiceConn conn;
    public static Handler conhandler;
    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case Constant.PLAY: {
                    int index = (int) msg.arg1;
                    initBottomPlayData(index);
                    break;
                }
            }
            return false;
        }
    });

    private void initBottomPlayData(int index) {

        ArrayList<MusicBean> musicBeans = MusicManager.getMusicBeans();
        if (musicBeans==null)return;
        MusicBean musicBean = musicBeans.get(index);

        ImageView songPic = findViewById(R.id.song1_pic);
        ImageView playOrStop = findViewById(R.id.playOrStop);
        if (MusicManager.musicControl.isCanPlay()) {
            playOrStop.setImageResource(R.drawable.pause);
        }

        TextView songSonger = findViewById(R.id.song1_songer);
        songSonger.setText(musicBean.getArtistName());
        TextView songname = findViewById(R.id.song1_name);
        songname.setText(musicBean.getTitle());
        Uri albumArtUri = MusicUtils.getAlbumArtUri(musicBean.getAlbumId());
        Bitmap bitmap = MusicUtils.decodeUri(CustomTitleActivity.this.getBaseContext(),albumArtUri,300,300);
        if (bitmap!=null) {
            songPic.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conhandler = handler;
        setContentView(R.layout.activity_custom_title);
        Intent intent = new Intent(this, MusicService.class);//创建意图对象
        conn = new MyServiceConn();                       //创建服务连接对象
        bindService(intent, conn, BIND_AUTO_CREATE);  //绑定服务
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        initBottomPlayData(MusicManager.getPosition());

        ImageView playOrStop = findViewById(R.id.playOrStop);
        playOrStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MusicManager.musicControl.isPlaying()) {
                    if (MusicManager.musicControl.isCanPlay()) {
                        MusicManager.musicControl.continueMusic();
                        playOrStop.setImageResource(R.drawable.pause);
                    }
                }else {
                    if (MusicManager.musicControl.isCanPlay()) {
                        MusicManager.musicControl.pausePlay();
                        playOrStop.setImageResource(R.drawable.play);
                    }
                }
            }
        });

        View songNext = findViewById(R.id.song1_next);
        songNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.playNext();
            }
        });
        findViewById(R.id.bottom_play_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomTitleActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });

    }

    class MyServiceConn implements ServiceConnection { //用于实现连接服务
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicManager.musicControl = (MusicControl) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}