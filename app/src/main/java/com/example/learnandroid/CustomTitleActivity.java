package com.example.learnandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.adapter.SectionsPagerAdapter;
import com.example.learnandroid.service.MusicControl;
import com.example.learnandroid.service.MusicService;
import com.google.android.material.tabs.TabLayout;

public class CustomTitleActivity extends AppCompatActivity {
    private MyServiceConn conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_title);
        Intent intent = new Intent(this, MusicService.class);//创建意图对象
        conn = new MyServiceConn();                       //创建服务连接对象
        bindService(intent, conn, BIND_AUTO_CREATE);  //绑定服务
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
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