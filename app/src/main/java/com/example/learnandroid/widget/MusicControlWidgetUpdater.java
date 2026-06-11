package com.example.learnandroid.widget;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.learnandroid.LoadingActivity;
import com.example.learnandroid.MusicMainActivity;
import com.example.learnandroid.R;
import com.example.learnandroid.application.utils.BitmapUtils;
import com.example.learnandroid.application.utils.PlaybackStateStore;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.Constant;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.service.MusicService;

public final class MusicControlWidgetUpdater {
    private static final float LARGE_WIDGET_MIN_HEIGHT_DP = 110f;
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
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = buildRemoteViews(context, appWidgetManager, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @NonNull
    private static RemoteViews buildRemoteViews(@NonNull Context context,
                                                @NonNull AppWidgetManager appWidgetManager,
                                                int appWidgetId) {
        boolean useLargeLayout = shouldUseLargeLayout(context, appWidgetManager, appWidgetId);
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),
                useLargeLayout ? R.layout.widget_music_control_large : R.layout.widget_music_control
        );
        MusicBean musicBean = resolveWidgetSong();
        boolean isPlaying = resolveWidgetPlaybackState();
        int duration = musicBean != null ? Math.max(musicBean.getDuration(), 0) : 0;
        int progress = musicBean != null ? resolveProgress(duration) : 0;

        remoteViews.setTextViewText(R.id.widget_song_name, getSongTitle(context, musicBean));
        remoteViews.setTextViewText(R.id.widget_song_artist, getSongArtist(context, musicBean));
        remoteViews.setTextViewText(R.id.widget_status_text, getStatusText(context, musicBean, isPlaying));
        remoteViews.setImageViewBitmap(R.id.widget_album_art, getAlbumArtBitmap(context, musicBean));
        remoteViews.setProgressBar(R.id.widget_progress, Math.max(duration, 100), Math.min(progress, Math.max(duration, 100)), false);
        remoteViews.setImageViewResource(
                R.id.widget_action_play_pause,
                isPlaying ? R.mipmap.ic_pause_white_36dp : R.mipmap.ic_play_white_36dp
        );
        if (useLargeLayout) {
            remoteViews.setTextViewText(R.id.widget_hint_text, getHintText(context, musicBean));
        }

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

    private static boolean shouldUseLargeLayout(@NonNull Context context,
                                                @NonNull AppWidgetManager appWidgetManager,
                                                int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 0);
        return minHeight >= dpToPx(context, LARGE_WIDGET_MIN_HEIGHT_DP);
    }

    private static int dpToPx(@NonNull Context context, float dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }

    @NonNull
    private static Intent buildOpenAppIntent(@NonNull Context context) {
        Intent intent = new Intent(
                context,
                hasMediaPermission(context) ? MusicMainActivity.class : LoadingActivity.class
        );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    private static boolean hasMediaPermission(@NonNull Context context) {
        String permission = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_AUDIO
                : Manifest.permission.READ_EXTERNAL_STORAGE;
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
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

    @Nullable
    private static MusicBean resolveWidgetSong() {
        MusicBean musicBean = MusicManager.getMusicBean();
        if (musicBean != null) {
            return musicBean;
        }
        return PlaybackStateStore.getSavedCurrentSong();
    }

    private static boolean resolveWidgetPlaybackState() {
        return MusicManager.musicController != null ? MusicManager.isPlaying() : PlaybackStateStore.wasPlaying();
    }

    @NonNull
    private static String getStatusText(@NonNull Context context,
                                        @Nullable MusicBean musicBean,
                                        boolean isPlaying) {
        if (musicBean == null) {
            return context.getString(R.string.widget_not_playing);
        }
        return context.getString(isPlaying ? R.string.widget_status_playing : R.string.widget_status_paused);
    }

    @NonNull
    private static String getHintText(@NonNull Context context, @Nullable MusicBean musicBean) {
        if (musicBean == null) {
            return context.getString(R.string.widget_tap_to_open);
        }
        return context.getString(R.string.widget_tap_to_open);
    }

    private static int resolveProgress(int duration) {
        if (duration <= 0) {
            return 0;
        }
        int progress = PlaybackStateStore.getSavedSeekPosition();
        try {
            if (MusicManager.musicController != null) {
                progress = (int) MusicManager.getCurrentPosition();
            }
        } catch (Exception ignored) {
        }
        return Math.max(progress, 0);
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

