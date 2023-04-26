package com.example.learnandroid;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * 用户需要一种简单的方法来返回到您应用程序的主屏幕。为此，请在应用栏上为除主要活动之外的所有活动提供一个向上 按钮。当用户选择向上按钮时，应用会导航到父活动。
 */
//注意：对应用中使用 aToolbar作为应用栏的每个 Activity 进行此更改。
public class LoadingActivity extends AppCompatActivity {

    public static final int REQUEST_CONDE =0xFFFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        myRequetPermission();
        Intent intent = new Intent(this, MusicMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void myRequetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            Toast.makeText(this,"您已经申请了权限!",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //判断我们的请求码，避免别的事件调用onRequestPermissionsResult,导致我们拿到本不该属于我们的数据
        if (requestCode==REQUEST_CONDE){
            // 如果请求被取消，则结果数组为空。
            if (grantResults.length > 0) {
                //循环一个一个地去判断结果
                for (int k=0;k<permissions.length;k++){

                    if (grantResults[k] == PackageManager.PERMISSION_GRANTED){
                        // 权限请求成功，抛出结果true
                        throwPermissionResults(permissions[k],true);
                    }

                    if (grantResults[k] == PackageManager.PERMISSION_DENIED){
                        // 权限请求失败，抛出结果false
                        throwPermissionResults(permissions[k],false);
                    }
                }
            } else {
                //没有任何授权结果,直接抛出结果false
                throwPermissionResults("Unknown_result",false);
            }
        }
    }


    public void throwPermissionResults(String permissionName, boolean isSuccess) {
        //拿到相应的权限，以及授权结果
        switch (permissionName){
            case Manifest.permission.CAMERA:
                Log.d("fxHou","CAMERA授权结果："+isSuccess);
                break;
            case Manifest.permission.BODY_SENSORS:
                Log.d("fxHou","BODY_SENSORS授权结果："+isSuccess);
                break;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                Log.d("fxHou","READ_EXTERNAL_STORAGE授权结果："+isSuccess);
                break;
        }
    }
//
//    public void buildNotification(){
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        RemoteViews rv = new RemoteViews(getPackageName(),R.layout.albmitem);
////        rv.setTextViewText(R.id.tv,"泡沫");//修改自定义View中的歌名
//        //修改自定义View中的图片(两种方法)
//        //        rv.setImageViewResource(R.id.iv,R.mipmap.ic_launcher);
////        rv.setImageViewBitmap(R.id.iv, BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
//        builder.setContent(rv);
//        Notification notification = builder.build();
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1,notification);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * 处理事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}