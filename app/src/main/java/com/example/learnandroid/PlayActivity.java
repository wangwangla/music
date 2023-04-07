package com.example.learnandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import com.example.learnandroid.main.PlayFragment;
import com.example.learnandroid.utils.ThemeUtils;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        replace(new PlayFragment());
        ThemeUtils.updateSystemBarContent(this,false);
        ThemeUtils.hideTopBg(this);

        View topBack = findViewById(R.id.top_back);
        topBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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