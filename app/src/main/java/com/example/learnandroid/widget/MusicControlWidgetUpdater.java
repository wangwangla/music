package com.example.learnandroid.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.learnandroid.LoadingActivity;
import com.example.learnandroid.R;
import com.example.learnandroid.application.utils.BitmapUtils;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.Constant;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.service.MusicService;

public final class MusicControlWidgetUpdater {
    private static final int REQUEST_OPEN_APP = 300;
    private static final int REQUEST_PREVIOUS = 301;
    private static final int REQUEST_PLAY_PAUSE = 302;
    private static final int REQUEST_NEXT = 303;

    private MusicControlWidgetUpdater() {
    }

    public static void updateAllWidgets(@NonNull Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, MusicControlWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        updateWidgets(context, appWidgetManager, appWidgetIds);
    }

    public static void updateWidgets(@NonNull Context context,
                                     @NonNull AppWidgetManager appWidgetManager,
                                     @Nullable int[] appWidgetIds) {
        if (appWidgetIds == null || appWidgetIds.length == 0) {
            return;
        }
        RemoteViews remoteViews = buildRemoteViews(context);
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @NonNull
    private static RemoteViews buildRemoteViews(@NonNull Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_music_control);
        MusicBean musicBean = MusicManager.getMusicBean();
        boolean isPlaying = MusicManager.isPlaying();

        remoteViews.setTextViewText(R.id.widget_song_name, getSongTitle(context, musicBean));
        remoteViews.setTextViewText(R.id.widget_song_artist, getSongArtist(context, musicBean));
        remoteViews.setImageViewBitmap(R.id.widget_album_art, getAlbumArtBitmap(context, musicBean));
        remoteViews.setImageViewResource(
                R.id.widget_action_play_pause,
                isPlaying ? R.mipmap.ic_pause_white_36dp : R.mipmap.ic_play_white_36dp
        );

        PendingIntent openAppPendingIntent = PendingIntent.getActivity(
                context,
                REQUEST_OPEN_APP,
                buildOpenAppIntent(context),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        remoteViews.setOnClickPendingIntent(R.id.widget_root, openAppPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_album_art, openAppPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_text_container, openAppPendingIntent);
        remoteViews.setOnClickPendingIntent(
                R.id.widget_action_prev,
                buildPlaybackPendingIntent(context, Constant.MUSIC_PRE, REQUEST_PREVIOUS)
        );
        remoteViews.setOnClickPendingIntent(
                R.id.widget_action_play_pause,
                buildPlaybackPendingIntent(context, Constant.MUSIC_STOP, REQUEST_PLAY_PAUSE)
        );
        remoteViews.setOnClickPendingIntent(
                R.id.widget_action_next,
                buildPlaybackPendingIntent(context, Constant.MUSIC_NEXT, REQUEST_NEXT)
        );
        return remoteViews;
    }

    @NonNull
    private static Intent buildOpenAppIntent(@NonNull Context context) {
        Intent intent = new Intent(context, LoadingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @NonNull
    private static PendingIntent buildPlaybackPendingIntent(@NonNull Context context,
                                                            @NonNull String action,
                                                            int requestCode) {
        Intent intent = new Intent(context, MusicService.class);
        intent.setAction(action);
        return PendingIntent.getService(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    @NonNull
    private static String getSongTitle(@NonNull Context context, @Nullable MusicBean musicBean) {
        if (musicBean == null || musicBean.getTitle() == null || musicBean.getTitle().trim().isEmpty()) {
            return context.getString(R.string.app_name);
        }
        return musicBean.getTitle();
    }

    @NonNull
    private static String getSongArtist(@NonNull Context context, @Nullable MusicBean musicBean) {
        if (musicBean == null || musicBean.getArtistName() == null || musicBean.getArtistName().trim().isEmpty()) {
            return context.getString(R.string.widget_not_playing);
        }
        return musicBean.getArtistName();
    }

    @NonNull
    private static Bitmap getAlbumArtBitmap(@NonNull Context context, @Nullable MusicBean musicBean) {
        Bitmap fallback = BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_image2);
        if (musicBean == null) {
            return fallback;
        }
        Uri albumArtUri = BitmapUtils.getAlbumArtUri(musicBean.getAlbumId());
        Bitmap albumArt = BitmapUtils.decodeUri(context, albumArtUri, 160, 160);
        return albumArt != null ? albumArt : fallback;
    }
}

