package com.example.learnandroid.notification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.learnandroid.MusicMainActivity;
import com.example.learnandroid.R;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 23:46
 */
class MusicMediaNotification {
    private static MusicMediaNotification instance;
    private Context mContext;

    public static MusicMediaNotification getInstance(Context context) {
        if(instance==null){
            instance=new MusicMediaNotification(context);
        }
        return instance;
    }

    private MusicMediaNotification(Context context){
        mContext=context;
    }

    public void CreateChannel(String channel_id,CharSequence channel_name,String description){
        //8.0以上版本通知适配
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel=new NotificationChannel(channel_id,channel_name, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager=mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * 返回一个前台通知
     * @param channel_id  通知渠道id，注意8.0创建通知的时候渠道id与此要匹配
     * @param musicPicture 数据对象
     * @param remoteViews 自定义通知样式的对象，但是与View不同，不提供findViewById方法，详细建议看看源码和官方文档
     * @return
     */

    public Notification createForeNotification(String channel_id,RemoteViews remoteViews){
        Intent intent=new Intent(mContext, MusicMainActivity.class);
        PendingIntent mainIntent=PendingIntent.getActivity(mContext,0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(mContext,channel_id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomBigContentView(remoteViews)
                .setContentIntent(mainIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder.build();
    }
}


