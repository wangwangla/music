package com.example.learnandroid.base;


import android.graphics.Color;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import kw.learn.mylibrary.edge.EdgeToEdgeCompat;


public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        EdgeToEdgeCompat.enable(this);
        Window window = getWindow();
        // 让内容占满系统栏区域  布局会延伸到系统栏下面
        // 之后需要自己处理 WindowInsets（比如你代码里 applySystemBars() 给根布局加 padding）
        WindowCompat.setDecorFitsSystemWindows(window, false);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);

        setContentView(getResourceId());
    }

    protected abstract int getResourceId();
}
