package com.example.learnandroid.notification;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.service.MusicService;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 22:27
 */
public class MusicNotification {
    String title = "music";
    String content = "learn music !";
    String ColorStr = "#53c95b";
    public MusicNotification(Context context){
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT < 11){
//            //不支持style  ，所以音乐播放器的控制部分是没有办法做了。
//            com.example.learnandroid
//                    .notification.lesslevel
//                    .NotificationCompat.Builder
//                    notificationCompat = new
//                    com.example.learnandroid.
//                            notification.lesslevel.
//                            NotificationCompat.Builder(context);
//            notificationCompat.setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(title)
//                    .setContentText(content);
//            notificationManager.notify(0, notificationCompat.build());
//        }else if (Build.VERSION.SDK_INT < 21){
//            androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(context);
//            builder.setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(title)
//                    .setContentText(content);
//            notificationManager.notify(0, builder.build());
//        }else if (Build.VERSION.SDK_INT<26){
//            androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(context);
//            Drawable drawable = context.getApplicationInfo().loadIcon(context.getPackageManager());
//            Bitmap bitmap = getBitmapFromDrawable(drawable);
//            builder.setSmallIcon(R.mipmap.ic_launcher)
//                    .setLargeIcon(bitmap)
//                    .setContentTitle(title)
//                    .setContentText(content)
//                    .setColor(Color.parseColor(ColorStr));
//            notificationManager.notify(0, builder.build());
//        }else {
//            NotificationChannel mChannel = new NotificationChannel(
//                    "music_channel", "music",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(mChannel);
//            Notification.Builder builder1 = new Notification.Builder(context, "music_channel");
//            Drawable drawable = context.getApplicationInfo().loadIcon(context.getPackageManager());
//            Bitmap bitmap = getBitmapFromDrawable(drawable);
//            builder1.setSmallIcon(R.mipmap.ic_launcher)
//                    .setLargeIcon(bitmap)
//                    .setContentTitle(title)
//                    .setContentText(content)
//                    .setColor(Color.parseColor(ColorStr));
//            notificationManager.notify(0, builder1.build());
//        }
//        Intent notificationIntent = new Intent(context, CustomTitleActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        MediaSession.Token mediaSession = new MediaSession.Token();
//        Notification notification = new NotificationCompat.Builder(context, "CHANNEL_ID")
//                // Show controls on lock screen even when user hides sensitive content.
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .setSmallIcon(R.drawable.ic_notification)
//                // Add media control buttons that invoke intents in your media service
//                .addAction(R.drawable.ic_skip_previous_black_24dp, "Previous", contentIntent) // #0
//                .addAction(R.drawable.ic_notification, "Pause", contentIntent)  // #1
//                .addAction(R.drawable.next, "Next",contentIntent)     // #2
//                // Apply the media style template
//                .setStyle(new Notification.MediaStyle()
//                        .setShowActionsInCompactView(1 /* #1: pause button */)
//                        .setMediaSession(mediaSession.getSessionToken()))
//                .setContentTitle("Wonderful music")
//                .setContentText("My Awesome Band")
//                .setLargeIcon(albumArtBitmap)
//                .build();
//        Drawable drawable = context.getApplicationInfo().loadIcon(context.getPackageManager());
//            Bitmap bitmap = getBitmapFromDrawable(drawable);
//
//        Intent nowPlayingIntent = NavigationUtils.getNowPlayingIntent(this);
////
//        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_notification)
//                .setLargeIcon(bitmap)
//                .setContentIntent(clickIntent)
//                .setContentTitle(getTrackName())
//                .setContentText(text)
//                .setWhen(mNotificationPostTime)
//                .addAction(R.drawable.ic_skip_previous_white_36dp,
//                        "",
//                        retrievePlaybackAction(PREVIOUS_ACTION))
//                .addAction(playButtonResId, "",
//                        retrievePlaybackAction(TOGGLEPAUSE_ACTION))
//                .addAction(R.drawable.ic_skip_next_white_36dp,
//                        "",
//                        retrievePlaybackAction(NEXT_ACTION));


    }


    private final PendingIntent retrievePlaybackAction(final String action) {
        final ComponentName serviceName = new ComponentName(MusicApplication.getMusicContent(), MusicService.class);
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);
        return PendingIntent.getService(MusicApplication.getMusicContent(), 0, intent, 0);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }
}
