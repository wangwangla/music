package com.example.learnandroid.base;


import android.graphics.Color;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnandroid.R;

import kw.learn.mylibrary.edge.EdgeToEdgeCompat;


public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdgeCompat.enable(this);
        setContentView(getResourceId());
    }

    /**
     * eg:
     *         viewInset(findViewById(R.id.load_root),(v, insets) -> {
     *             ViewCompat.onApplyWindowInsets(v, insets);
     *             Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
     *             v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
     *             return insets;
     *         });
     * @param view
     * @param listener
     */
    public void viewInset(View view, OnApplyWindowInsetsListener listener) {
        ViewCompat.setOnApplyWindowInsetsListener(view, listener);
    }

    protected abstract int getResourceId();
}
