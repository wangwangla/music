package com.example.learnandroid.application.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.data.SongLoader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PlaybackStateStore {
    private static final String PREFS_NAME = "playback_state_store";
    private static final String KEY_QUEUE_IDS = "queue_ids";
    private static final String KEY_CURRENT_INDEX = "current_index";
    private static final String KEY_CURRENT_SONG_ID = "current_song_id";
    private static final String KEY_CURRENT_PATH = "current_path";
    private static final String KEY_SEEK_POSITION = "seek_position";
    private static final String KEY_PLAY_STYLE = "play_style";
    private static final String KEY_WAS_PLAYING = "was_playing";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_ALBUM = "album";
    private static final String KEY_ALBUM_ID = "album_id";
    private static final String KEY_DURATION = "duration";

    private PlaybackStateStore() {
    }

    public static void saveSnapshot(@Nullable List<MusicBean> queue,
                                    int currentIndex,
                                    long currentSongId,
                                    @Nullable String currentPath,
                                    boolean wasPlaying,
                                    int seekPosition,
                                    int playStyle) {
        SharedPreferences.Editor editor = prefs().edit();
        editor.putString(KEY_QUEUE_IDS, serializeQueueIds(queue));
        editor.putInt(KEY_CURRENT_INDEX, currentIndex);
        editor.putLong(KEY_CURRENT_SONG_ID, currentSongId);
        editor.putString(KEY_CURRENT_PATH, currentPath == null ? "" : currentPath);
        editor.putInt(KEY_SEEK_POSITION, Math.max(seekPosition, 0));
        editor.putInt(KEY_PLAY_STYLE, playStyle);
        editor.putBoolean(KEY_WAS_PLAYING, wasPlaying);

        MusicBean currentSong = findCurrentSong(queue, currentSongId, currentPath, currentIndex);
        if (currentSong != null) {
            editor.putString(KEY_TITLE, safeString(currentSong.getTitle()));
            editor.putString(KEY_ARTIST, safeString(currentSong.getArtistName()));
            editor.putString(KEY_ALBUM, safeString(currentSong.getAlbumName()));
            editor.putLong(KEY_ALBUM_ID, currentSong.getAlbumId());
            editor.putInt(KEY_DURATION, currentSong.getDuration());
        } else {
            editor.putString(KEY_TITLE, "");
            editor.putString(KEY_ARTIST, "");
            editor.putString(KEY_ALBUM, "");
            editor.putLong(KEY_ALBUM_ID, -1L);
            editor.putInt(KEY_DURATION, 0);
        }
        editor.apply();
    }

    @NonNull
    public static RestoredState restoreState() {
        Snapshot snapshot = readSnapshot();
        ArrayList<MusicBean> allSongs = SongLoader.loadAllSongList();
        ArrayList<MusicBean> restoredQueue = rebuildQueue(allSongs, snapshot.queueIds);
        MusicBean currentSong = findCurrentSong(restoredQueue, snapshot.currentSongId, snapshot.currentPath, snapshot.currentIndex);
        int currentIndex = currentSong == null ? normalizeIndex(snapshot.currentIndex, restoredQueue.size()) : restoredQueue.indexOf(currentSong);
        if (currentSong == null && currentIndex >= 0 && currentIndex < restoredQueue.size()) {
            currentSong = restoredQueue.get(currentIndex);
        }
        return new RestoredState(restoredQueue, currentSong, currentIndex, snapshot.seekPosition, snapshot.playStyle, snapshot.wasPlaying);
    }

    @Nullable
    public static MusicBean getSavedCurrentSong() {
        Snapshot snapshot = readSnapshot();
        if (TextUtils.isEmpty(snapshot.title) && TextUtils.isEmpty(snapshot.artist) && snapshot.albumId < 0) {
            return null;
        }
        return new MusicBean(
                snapshot.currentSongId,
                snapshot.albumId,
                -1L,
                safeString(snapshot.title),
                safeString(snapshot.artist),
                safeString(snapshot.album),
                snapshot.duration,
                0,
                safeString(snapshot.currentPath)
        );
    }

    public static boolean wasPlaying() {
        return prefs().getBoolean(KEY_WAS_PLAYING, false);
    }

    public static int getSavedSeekPosition() {
        return prefs().getInt(KEY_SEEK_POSITION, 0);
    }

    public static int getSavedPlayStyle() {
        return prefs().getInt(KEY_PLAY_STYLE, 0);
    }

    private static int normalizeIndex(int currentIndex, int size) {
        if (size <= 0) {
            return -1;
        }
        if (currentIndex < 0) {
            return 0;
        }
        if (currentIndex >= size) {
            return size - 1;
        }
        return currentIndex;
    }

    @Nullable
    private static MusicBean findCurrentSong(@Nullable List<MusicBean> queue,
                                             long currentSongId,
                                             @Nullable String currentPath,
                                             int currentIndex) {
        if (queue == null || queue.isEmpty()) {
            return null;
        }
        if (currentSongId >= 0) {
            for (MusicBean musicBean : queue) {
                if (musicBean.getId() == currentSongId) {
                    return musicBean;
                }
            }
        }
        if (!TextUtils.isEmpty(currentPath)) {
            for (MusicBean musicBean : queue) {
                if (currentPath.equals(musicBean.getPath())) {
                    return musicBean;
                }
            }
        }
        int normalizedIndex = normalizeIndex(currentIndex, queue.size());
        if (normalizedIndex >= 0 && normalizedIndex < queue.size()) {
            return queue.get(normalizedIndex);
        }
        return null;
    }

    @NonNull
    private static ArrayList<MusicBean> rebuildQueue(@Nullable List<MusicBean> allSongs, @Nullable List<Long> queueIds) {
        ArrayList<MusicBean> queue = new ArrayList<>();
        if (allSongs == null || allSongs.isEmpty()) {
            return queue;
        }
        if (queueIds == null || queueIds.isEmpty()) {
            queue.addAll(allSongs);
            return queue;
        }
        Map<Long, MusicBean> songMap = new LinkedHashMap<>();
        for (MusicBean musicBean : allSongs) {
            songMap.put(musicBean.getId(), musicBean);
        }
        for (Long queueId : queueIds) {
            if (queueId == null) {
                continue;
            }
            MusicBean musicBean = songMap.remove(queueId);
            if (musicBean != null) {
                queue.add(musicBean);
            }
        }
        queue.addAll(songMap.values());
        return queue;
    }

    @NonNull
    private static String serializeQueueIds(@Nullable List<MusicBean> queue) {
        if (queue == null || queue.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (MusicBean musicBean : queue) {
            if (musicBean == null) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(musicBean.getId());
        }
        return builder.toString();
    }

    @NonNull
    private static Snapshot readSnapshot() {
        SharedPreferences sharedPreferences = prefs();
        Snapshot snapshot = new Snapshot();
        snapshot.queueIds = parseQueueIds(sharedPreferences.getString(KEY_QUEUE_IDS, ""));
        snapshot.currentIndex = sharedPreferences.getInt(KEY_CURRENT_INDEX, -1);
        snapshot.currentSongId = sharedPreferences.getLong(KEY_CURRENT_SONG_ID, -1L);
        snapshot.currentPath = sharedPreferences.getString(KEY_CURRENT_PATH, "");
        snapshot.seekPosition = sharedPreferences.getInt(KEY_SEEK_POSITION, 0);
        snapshot.playStyle = sharedPreferences.getInt(KEY_PLAY_STYLE, 0);
        snapshot.wasPlaying = sharedPreferences.getBoolean(KEY_WAS_PLAYING, false);
        snapshot.title = sharedPreferences.getString(KEY_TITLE, "");
        snapshot.artist = sharedPreferences.getString(KEY_ARTIST, "");
        snapshot.album = sharedPreferences.getString(KEY_ALBUM, "");
        snapshot.albumId = sharedPreferences.getLong(KEY_ALBUM_ID, -1L);
        snapshot.duration = sharedPreferences.getInt(KEY_DURATION, 0);
        return snapshot;
    }

    @NonNull
    private static ArrayList<Long> parseQueueIds(@Nullable String value) {
        ArrayList<Long> queueIds = new ArrayList<>();
        if (TextUtils.isEmpty(value)) {
            return queueIds;
        }
        String[] parts = value.split(",");
        for (String part : parts) {
            if (TextUtils.isEmpty(part)) {
                continue;
            }
            try {
                queueIds.add(Long.parseLong(part));
            } catch (NumberFormatException ignored) {
            }
        }
        return queueIds;
    }

    @NonNull
    private static SharedPreferences prefs() {
        return MusicApplication.getMusicContent().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @NonNull
    private static String safeString(@Nullable String value) {
        return value == null ? "" : value;
    }

    private static final class Snapshot {
        private ArrayList<Long> queueIds;
        private int currentIndex;
        private long currentSongId;
        private String currentPath;
        private int seekPosition;
        private int playStyle;
        private boolean wasPlaying;
        private String title;
        private String artist;
        private String album;
        private long albumId;
        private int duration;
    }

    public static final class RestoredState {
        private final ArrayList<MusicBean> queue;
        @Nullable
        private final MusicBean currentSong;
        private final int currentIndex;
        private final int seekPosition;
        private final int playStyle;
        private final boolean wasPlaying;

        RestoredState(@NonNull ArrayList<MusicBean> queue,
                      @Nullable MusicBean currentSong,
                      int currentIndex,
                      int seekPosition,
                      int playStyle,
                      boolean wasPlaying) {
            this.queue = queue;
            this.currentSong = currentSong;
            this.currentIndex = currentIndex;
            this.seekPosition = seekPosition;
            this.playStyle = playStyle;
            this.wasPlaying = wasPlaying;
        }

        @NonNull
        public ArrayList<MusicBean> getQueue() {
            return queue;
        }

        @Nullable
        public MusicBean getCurrentSong() {
            return currentSong;
        }

        public int getCurrentIndex() {
            return currentIndex;
        }

        public int getSeekPosition() {
            return seekPosition;
        }

        public int getPlayStyle() {
            return playStyle;
        }

        public boolean wasPlaying() {
            return wasPlaying;
        }
    }
}

