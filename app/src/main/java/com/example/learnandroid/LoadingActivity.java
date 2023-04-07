package com.example.learnandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;

/**
 * 用户需要一种简单的方法来返回到您应用程序的主屏幕。为此，请在应用栏上为除主要活动之外的所有活动提供一个向上 按钮。当用户选择向上按钮时，应用会导航到父活动。
 */

/**
 * 自定义标题
 */
//注意：对应用中使用 aToolbar作为应用栏的每个 Activity 进行此更改。
public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Intent intent = new Intent(this, MusicMainActivity.class);
        startActivity(intent);
        finish();
//        buildNotification();
    }

    public void buildNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        RemoteViews rv = new RemoteViews(getPackageName(),R.layout.albmitem);
//        rv.setTextViewText(R.id.tv,"泡沫");//修改自定义View中的歌名
        //修改自定义View中的图片(两种方法)
        //        rv.setImageViewResource(R.id.iv,R.mipmap.ic_launcher);
//        rv.setImageViewBitmap(R.id.iv, BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setContent(rv);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);
    }

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