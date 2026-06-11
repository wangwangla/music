package com.example.learnandroid.service;

import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.MediaBrowserServiceCompat;

import com.example.learnandroid.MusicMainActivity;
import com.example.learnandroid.R;
import com.example.learnandroid.application.utils.PlaybackStateStore;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.application.utils.BitmapUtils;
import com.example.learnandroid.constant.Constant;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.session.SessionUtils;
import com.example.learnandroid.widget.MusicControlWidgetUpdater;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class MusicService extends MediaBrowserServiceCompat {
    //添加日志的TAG常量
    private static MusicService instance;
    private static final String MEDIA_CHANNEL_ID = "XXX";
    private static final int MEDIA_NOTIFICATION_ID = 1001;
    private MediaPlayer player;
    private MusicControl musicControl;
    private SessionUtils sessionUtils;
    private NotificationManager notificationManager;
    private boolean isForeground;
    private boolean isShuttingDown;
    private final Set<Long> likedSongIds = new HashSet<>();
    private final Runnable sessionSyncRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMediaSessionStateInternal();
        }
    };

    public MusicService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        player = new MediaPlayer();//创建音乐播放器对象
        musicControl = new MusicControl(player);
        MusicManager.musicController = musicControl;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        sessionUtils = new SessionUtils(this);
        setSessionToken(sessionUtils.getSessionToken());
        MusicManager.addUpdateView(sessionSyncRunnable);
        if (PlaybackStateStore.getSavedCurrentSong() != null) {
            restorePlaybackStateIfNeeded();
        }
        refreshMediaSessionStateInternal();
    }

    @Nullable
    public static MediaSessionCompat.Token getActiveSessionToken() {
        if (instance == null || instance.sessionUtils == null) {
            return null;
        }
        return instance.sessionUtils.getSessionToken();
    }

    public static void refreshMediaSessionState() {
        if (instance != null) {
            instance.refreshMediaSessionStateInternal();
        }
    }

    private void refreshMediaSessionStateInternal() {
        if (sessionUtils == null || sessionUtils.getmSession() == null) {
            MusicControlWidgetUpdater.updateAllWidgets(this);
            return;
        }
        MusicBean musicBean = MusicManager.getMusicBean();
        Bitmap albumArt = getAlbumArtBitmap(musicBean);
        if (musicBean != null) {
            sessionUtils.setMetadata(new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, musicBean.getTitle())
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, musicBean.getArtistName())
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, musicBean.getAlbumName())
                    .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, MusicManager.getDuration())
                    .build());
        }
        long position = 0L;
        try {
            position = MusicManager.getCurrentPosition();
        } catch (Exception ignored) {
        }
        int state = MusicManager.isPlaying()
                ? PlaybackStateCompat.STATE_PLAYING
                : PlaybackStateCompat.STATE_PAUSED;
        float speed = MusicManager.isPlaying() ? 1f : 0f;
        long actions = PlaybackStateCompat.ACTION_PLAY
                | PlaybackStateCompat.ACTION_PAUSE
                | PlaybackStateCompat.ACTION_PLAY_PAUSE
                | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                | PlaybackStateCompat.ACTION_STOP
                | PlaybackStateCompat.ACTION_SEEK_TO;
        sessionUtils.getmSession().setPlaybackState(new PlaybackStateCompat.Builder()
                .setActions(actions)
                .setState(state, position, speed)
                .build());
        updateServiceNotification();
        MusicControlWidgetUpdater.updateAllWidgets(this);
    }

    private void updateServiceNotification() {
        if (notificationManager == null) {
            return;
        }
        createNotificationChannel();
        Notification notification = buildMediaNotification();
        if (MusicManager.isPlaying()) {
            if (!isForeground) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startForeground(
                            MEDIA_NOTIFICATION_ID,
                            notification,
                            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                    );
                } else {
                    startForeground(MEDIA_NOTIFICATION_ID, notification);
                }
                isForeground = true;
            } else {
                notificationManager.notify(MEDIA_NOTIFICATION_ID, notification);
            }
        } else {
            if (isForeground) {
                stopForeground(false);
                isForeground = false;
            }
            notificationManager.notify(MEDIA_NOTIFICATION_ID, notification);
        }
    }

    private Notification buildMediaNotification() {
        MusicBean musicBean = MusicManager.getMusicBean();
        String title = musicBean != null ? musicBean.getTitle() : getString(R.string.app_name);
        String artist = musicBean != null ? musicBean.getArtistName() : "未在播放";
        int playRes = MusicManager.isPlaying() ? R.mipmap.ic_pause_white_36dp : R.mipmap.ic_play_white_36dp;
        boolean isLiked = isCurrentSongLiked(musicBean);
        int likeRes = isLiked ? R.mipmap.ic_notification_favorite_added : R.mipmap.ic_notification_favorite_add;

        Intent openIntent = new Intent(this, MusicMainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                99,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MEDIA_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_notification)
                .setLargeIcon(getAlbumArtBitmap(musicBean))
                .setContentTitle(title)
                .setContentText(artist)
                .setContentIntent(contentIntent)
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setOngoing(MusicManager.isPlaying())
                .addAction(R.mipmap.ic_skip_previous_white_36dp,
                        "prev", retrievePlaybackAction(Constant.MUSIC_PRE, 0))
                .addAction(playRes,
                        "play_pause", retrievePlaybackAction(Constant.MUSIC_STOP, 1))
                .addAction(R.mipmap.ic_skip_next_white_36dp,
                        "next", retrievePlaybackAction(Constant.MUSIC_NEXT, 2))
                .addAction(likeRes,
                        "like", retrievePlaybackAction(Constant.MUSIC_LIKE_TOGGLE, 3));

        builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(sessionUtils.getSessionToken())
                .setShowActionsInCompactView(0, 1, 2));
        return builder.build();
    }

    private boolean isCurrentSongLiked(@Nullable MusicBean musicBean) {
        return musicBean != null && likedSongIds.contains(musicBean.getId());
    }

    private void toggleCurrentSongLike() {
        MusicBean musicBean = MusicManager.getMusicBean();
        if (musicBean == null) {
            return;
        }
        long songId = musicBean.getId();
        if (likedSongIds.contains(songId)) {
            likedSongIds.remove(songId);
        } else {
            likedSongIds.add(songId);
        }
    }

    private Bitmap getAlbumArtBitmap(@Nullable MusicBean musicBean) {
        Bitmap fallback = BitmapFactory.decodeResource(getResources(), R.mipmap.default_image2);
        if (musicBean == null) {
            return fallback;
        }
        Uri albumArtUri = BitmapUtils.getAlbumArtUri(musicBean.getAlbumId());
        Bitmap albumArt = BitmapUtils.decodeUri(this, albumArtUri, 300, 300);
        return albumArt != null ? albumArt : fallback;
    }

    private PendingIntent retrievePlaybackAction(final String action, int code) {
        Intent intent = new Intent(action);
        intent.setClass(this, MusicService.class);
        return PendingIntent.getService(this, code, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private boolean restorePlaybackStateIfNeeded() {
        if (MusicManager.hasSongList() && MusicManager.musicController != null && MusicManager.getMusicBean() != null) {
            return true;
        }
        PlaybackStateStore.RestoredState restoredState = PlaybackStateStore.restoreState();
        if (restoredState.getQueue().isEmpty()) {
            return false;
        }
        Constant.playStyle = restoredState.getPlayStyle();
        MusicManager.setSongList(restoredState.getQueue());
        int currentIndex = restoredState.getCurrentIndex();
        if (currentIndex < 0 || currentIndex >= restoredState.getQueue().size()) {
            currentIndex = 0;
        }
        MusicManager.setCurrentPlayId(-1L);
        if (!MusicManager.setData(currentIndex)) {
            return false;
        }
        if (restoredState.getSeekPosition() > 0) {
            try {
                MusicManager.seekTo(restoredState.getSeekPosition());
            } catch (Exception ignored) {
            }
        }
        MusicManager.persistStateSnapshot();
        return true;
    }

    private boolean needsRestoreForAction(@Nullable String action) {
        return Constant.MUSIC_PRE.equals(action)
                || Constant.MUSIC_NEXT.equals(action)
                || Constant.MUSIC_STOP.equals(action)
                || Constant.MUSIC_LIKE_TOGGLE.equals(action);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    MEDIA_CHANNEL_ID,
                    "Music",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void shutdownService(boolean removeNotification) {
        if (isShuttingDown) {
            return;
        }
        isShuttingDown = true;
        MusicManager.removeRunnable(sessionSyncRunnable);
        MusicManager.musicController = null;
        musicControl = null;
        if (sessionUtils != null && sessionUtils.getmSession() != null) {
            try {
                sessionUtils.getmSession().setActive(false);
                sessionUtils.getmSession().release();
            } catch (Exception ignored) {
            }
            sessionUtils = null;
        }
        if (player != null) {
            try {
                if (player.isPlaying()) {
                    player.stop();
                }
            } catch (Exception ignored) {
            }
            try {
                player.release();
            } catch (Exception ignored) {
            }
            player = null;
        }
        if (isForeground) {
            stopForeground(true);
            isForeground = false;
        } else if (removeNotification && notificationManager != null) {
            notificationManager.cancel(MEDIA_NOTIFICATION_ID);
        }
        if (removeNotification && notificationManager != null) {
            notificationManager.cancel(MEDIA_NOTIFICATION_ID);
        }
        MusicControlWidgetUpdater.updateAllWidgets(this);
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        if (intent == null || intent.getAction() == null) {
            return START_NOT_STICKY;
        }
        boolean shouldRefreshUi = false;
        try {
            String action = intent.getAction();
            if (needsRestoreForAction(action)) {
                restorePlaybackStateIfNeeded();
            }
            if (Constant.MUSIC_PRE.equals(action)) {
                MusicManager.playPre();
                shouldRefreshUi = true;
            } else if (Constant.MUSIC_NEXT.equals(action)) {
                MusicManager.playNext();
                shouldRefreshUi = true;
            } else if (Constant.MUSIC_STOP.equals(action)) {
                if (MusicManager.isPlaying()) {
                    MusicManager.pausePlay();
                } else {
                    MusicManager.continuePlay();
                }
                shouldRefreshUi = true;
            } else if (Constant.MUSIC_LIKE_TOGGLE.equals(action)) {
                toggleCurrentSongLike();
                shouldRefreshUi = true;
            }
        } catch (Exception ignored) {
        }
        if (shouldRefreshUi) {
            refreshMediaSessionStateInternal();
            sendBroadcast(new Intent(Constant.UP_DATE_BOTTOM));
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        shutdownService(true);
        stopSelf();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicControl;
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
//        return null;
        return new BrowserRoot(clientPackageName, null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        shutdownService(true);
    }
}
