package com.example.learnandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.learnandroid.main.GeciFragment;
import com.example.learnandroid.main.PlayFragment;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        View song = findViewById(R.id.song);
        replace(new PlayFragment());
        song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new PlayFragment());
            }
        });
    }

    public void replace(Fragment fragment){
        //调用getSupportFragmentManager()方法获取fragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager调用beginTransaction()开启一个事务
        FragmentTransaction transition = fragmentManager.beginTransaction();
        //向容器添加或者Fragment
        transition.replace(R.id.play_view, fragment);
        //调用commit()方法进行事务提交
        transition.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}