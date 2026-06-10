package com.example.learnandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import kw.learn.mylibrary.activity.BaseActivity;
import kw.learn.mylibrary.permission.PermissionUtils;

/**
 * 用户需要一种简单的方法来返回到您应用程序的主屏幕。为此，请在应用栏上为除主要活动之外的所有活动提供一个
 * 向上 按钮。当用户选择向上按钮时，应用会导航到父活动。
 *
 * 注意：对应用中使用 aToolbar作为应用栏的每个 Activity 进行此更改。
 */
public class LoadingActivity extends BaseActivity {
    public static final int REQUEST_CONDE =0xFFFF;
    private static final long GO_MAIN_DELAY_MS = 1000L;
    private boolean hasScheduledGoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPerssions();
        layoutAreas();
    }

    private void requestPerssions() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{
                    Manifest.permission.READ_MEDIA_AUDIO
                    ,Manifest.permission.POST_NOTIFICATIONS};
        } else {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        if (PermissionUtils.checkPermission(this,
                permissions,
                REQUEST_CONDE)) {
            scheduleGoMainOnce();
        }
    }

    private void layoutAreas() {
        viewInset(findViewById(R.id.load_root),(v, insets) -> {
            ViewCompat.onApplyWindowInsets(v, insets);
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_loading;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断我们的请求码，避免别的事件调用onRequestPermissionsResult,导致我们拿到本不该属于我们的数据
        if (requestCode==REQUEST_CONDE){
            // 如果请求被取消，则结果数组为空。
            boolean isSuccess = true;
            if (grantResults.length > 0) {
                //循环一个一个地去判断结果
                for (int k=0;k<permissions.length;k++){
                    if (grantResults[k] == PackageManager.PERMISSION_GRANTED){
                        // 权限请求成功，抛出结果true
                        throwPermissionResults(permissions[k],true);
                    }
                    if (grantResults[k] == PackageManager.PERMISSION_DENIED){
                        // 权限请求失败，抛出结果false
                        isSuccess = false;
                        throwPermissionResults(permissions[k],false);
                    }
                }
            } else {
                isSuccess = false;
                //没有任何授权结果,直接抛出结果false
                throwPermissionResults("Unknown_result",false);
            }
            if (isSuccess){
                scheduleGoMainOnce();
            }
        }
    }

    private void scheduleGoMainOnce() {
        if (hasScheduledGoMain) {
            return;
        }
        hasScheduledGoMain = true;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity();
            }
        }, GO_MAIN_DELAY_MS);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MusicMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void throwPermissionResults(String permissionName, boolean isSuccess) {
        //拿到相应的权限，以及授权结果
        Log.d("fxHou",permissionName+"授权结果："+isSuccess);
    }
}