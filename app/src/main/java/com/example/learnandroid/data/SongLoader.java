package com.example.learnandroid.data;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/11 16:28
 */
public class SongLoader {
    private static int loadType = 0;
    private static SaoMiaoMusicInterface instance;
    private static final ArrayList<MusicBean> cachedSongs = new ArrayList<>();

    public static void setLoadType(int _loadType) {
        loadType = _loadType;
    }

    public static ArrayList<MusicBean> loadAllSongList() {
        return loadAllSongList(false);
    }

    public static synchronized ArrayList<MusicBean> loadAllSongList(boolean forceRefresh) {
        if (!forceRefresh && !cachedSongs.isEmpty()) {
            return new ArrayList<>(cachedSongs);
        }
        getSaoMiaoMusicInstance().findMusic();
        cacheSongs(getSaoMiaoMusicInstance().getMusicBeans());
        if (Constant.NO_SET == 0){
            Intent intent = new Intent(Constant.UP_DATE_BOTTOM);
            MusicApplication.getMusicContent().sendBroadcast(intent);
        }
        return new ArrayList<>(cachedSongs);
    }

    @NonNull
    public static synchronized ArrayList<MusicBean> getCachedSongList() {
        return new ArrayList<>(cachedSongs);
    }

    public static synchronized void cacheSongs(@Nullable List<MusicBean> songs) {
        cachedSongs.clear();
        if (songs != null) {
            cachedSongs.addAll(songs);
        }
    }

    public static MusicBean loadSongById(long songId) {
        if (songId < 0) {
            return null;
        }
        String selection = MediaStore.Audio.Media._ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(songId) };
        return querySingleSong(selection, selectionArgs, null);
    }

    @Nullable
    public static MusicBean loadSongByPath(@Nullable String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        String selection = MediaStore.Audio.Media.DATA + "=?";
        return querySingleSong(selection, new String[]{path}, null);
    }

    @Nullable
    public static MusicBean loadSongByMetadata(@Nullable String title,
                                               @Nullable String artist,
                                               @Nullable String album,
                                               int duration) {
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return null;
        }
        ArrayList<MusicBean> candidates = new ArrayList<>();
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(artist) && !TextUtils.isEmpty(album)) {
            candidates.addAll(querySongs(
                    MediaStore.Audio.Media.TITLE + "=? AND "
                            + MediaStore.Audio.Media.ARTIST + "=? AND "
                            + MediaStore.Audio.Media.ALBUM + "=?",
                    new String[]{title, artist, album},
                    null));
        }
        if (candidates.isEmpty() && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(artist)) {
            candidates.addAll(querySongs(
                    MediaStore.Audio.Media.TITLE + "=? AND " + MediaStore.Audio.Media.ARTIST + "=?",
                    new String[]{title, artist},
                    null));
        }
        if (candidates.isEmpty() && !TextUtils.isEmpty(title)) {
            candidates.addAll(querySongs(
                    MediaStore.Audio.Media.TITLE + "=?",
                    new String[]{title},
                    null));
        }
        return chooseBestMatch(candidates, title, artist, album, duration);
    }

    @Nullable
    public static MusicBean restoreSongFromSnapshot(@Nullable MusicBean snapshotSong) {
        MusicBean resolved = resolveSongInternal(snapshotSong, false);
        if (resolved != null) {
            return resolved;
        }
        return resolveAgainstLibrary(snapshotSong, loadAllSongList(true));
    }

    @NonNull
    public static ArrayList<MusicBean> restoreQueueFromSnapshot(@Nullable List<MusicBean> snapshotQueue) {
        ArrayList<MusicBean> restoredQueue = new ArrayList<>();
        ArrayList<MusicBean> unresolved = new ArrayList<>();
        if (snapshotQueue == null || snapshotQueue.isEmpty()) {
            return restoredQueue;
        }
        for (MusicBean snapshotSong : snapshotQueue) {
            MusicBean resolved = resolveSongInternal(snapshotSong, false);
            if (resolved != null) {
                addIfAbsent(restoredQueue, resolved);
            } else if (snapshotSong != null) {
                unresolved.add(snapshotSong);
            }
        }
        if (!unresolved.isEmpty()) {
            ArrayList<MusicBean> allSongs = loadAllSongList(true);
            for (MusicBean snapshotSong : unresolved) {
                MusicBean resolved = resolveAgainstLibrary(snapshotSong, allSongs);
                if (resolved != null) {
                    addIfAbsent(restoredQueue, resolved);
                }
            }
        }
        return restoredQueue;
    }

    @Nullable
    private static MusicBean resolveSongInternal(@Nullable MusicBean snapshotSong, boolean allowLibraryFallback) {
        if (snapshotSong == null) {
            return null;
        }
        if (isPlayable(snapshotSong)) {
            return snapshotSong;
        }
        MusicBean cachedMatch = resolveAgainstLibrary(snapshotSong, cachedSongs);
        if (cachedMatch != null) {
            return cachedMatch;
        }
        MusicBean songById = loadSongById(snapshotSong.getId());
        if (isPlayable(songById)) {
            return songById;
        }
        MusicBean songByPath = loadSongByPath(snapshotSong.getPath());
        if (isPlayable(songByPath)) {
            return songByPath;
        }
        MusicBean songByMetadata = loadSongByMetadata(
                snapshotSong.getTitle(),
                snapshotSong.getArtistName(),
                snapshotSong.getAlbumName(),
                snapshotSong.getDuration()
        );
        if (isPlayable(songByMetadata)) {
            return songByMetadata;
        }
        if (allowLibraryFallback) {
            return resolveAgainstLibrary(snapshotSong, loadAllSongList(true));
        }
        return null;
    }

    @Nullable
    private static MusicBean resolveAgainstLibrary(@Nullable MusicBean snapshotSong,
                                                   @Nullable List<MusicBean> librarySongs) {
        if (snapshotSong == null || librarySongs == null || librarySongs.isEmpty()) {
            return null;
        }
        for (MusicBean musicBean : librarySongs) {
            if (musicBean.getId() == snapshotSong.getId() && isPlayable(musicBean)) {
                return musicBean;
            }
        }
        if (!TextUtils.isEmpty(snapshotSong.getPath())) {
            for (MusicBean musicBean : librarySongs) {
                if (snapshotSong.getPath().equals(musicBean.getPath()) && isPlayable(musicBean)) {
                    return musicBean;
                }
            }
        }
        return chooseBestMatch(
                librarySongs,
                snapshotSong.getTitle(),
                snapshotSong.getArtistName(),
                snapshotSong.getAlbumName(),
                snapshotSong.getDuration()
        );
    }

    @Nullable
    private static MusicBean chooseBestMatch(@Nullable List<MusicBean> candidates,
                                             @Nullable String title,
                                             @Nullable String artist,
                                             @Nullable String album,
                                             int duration) {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }
        MusicBean bestMatch = null;
        int bestScore = Integer.MIN_VALUE;
        for (MusicBean musicBean : candidates) {
            if (!isPlayable(musicBean)) {
                continue;
            }
            int score = 0;
            if (equalsNormalized(musicBean.getTitle(), title)) {
                score += 4;
            }
            if (equalsNormalized(musicBean.getArtistName(), artist)) {
                score += 3;
            }
            if (equalsNormalized(musicBean.getAlbumName(), album)) {
                score += 2;
            }
            if (duration > 0 && Math.abs(musicBean.getDuration() - duration) <= 2000) {
                score += 2;
            }
            if (score > bestScore) {
                bestScore = score;
                bestMatch = musicBean;
            }
        }
        return bestScore > 0 ? bestMatch : null;
    }

    private static boolean equalsNormalized(@Nullable String first, @Nullable String second) {
        if (TextUtils.isEmpty(first) || TextUtils.isEmpty(second)) {
            return false;
        }
        return first.trim().equalsIgnoreCase(second.trim());
    }

    private static void addIfAbsent(@NonNull ArrayList<MusicBean> queue, @NonNull MusicBean candidate) {
        for (MusicBean musicBean : queue) {
            if (musicBean.getId() == candidate.getId()) {
                return;
            }
            if (!TextUtils.isEmpty(musicBean.getPath()) && musicBean.getPath().equals(candidate.getPath())) {
                return;
            }
        }
        queue.add(candidate);
    }

    private static boolean isPlayable(@Nullable MusicBean song) {
        if (song == null || TextUtils.isEmpty(song.getPath())) {
            return false;
        }
        try {
            return new File(song.getPath()).exists();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Nullable
    private static MusicBean querySingleSong(@Nullable String selection,
                                             @Nullable String[] selectionArgs,
                                             @Nullable String sortOrder) {
        ArrayList<MusicBean> songs = querySongs(selection, selectionArgs, sortOrder);
        return songs.isEmpty() ? null : songs.get(0);
    }

    @NonNull
    private static ArrayList<MusicBean> querySongs(@Nullable String selection,
                                                   @Nullable String[] selectionArgs,
                                                   @Nullable String sortOrder) {
        ArrayList<MusicBean> musicBeans = new ArrayList<>();
        Cursor cursor = MusicApplication.getMusicContent().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                selectionArgs,
                sortOrder
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicBean musicBean = mapCursorToMusicBean(cursor);
                if (musicBean != null) {
                    musicBeans.add(musicBean);
                }
            }
            cursor.close();
        }
        return musicBeans;
    }

    @Nullable
    private static MusicBean mapCursorToMusicBean(@NonNull Cursor cursor) {
        try {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            int trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
            long artistId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
            long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            return new MusicBean(id, albumId, artistId, title, artist, album, duration, trackNumber, path);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static SaoMiaoMusicInterface getSaoMiaoMusicInstance(){
        if (instance == null) {
            if (loadType == 0) {
                instance = new ContentResolverFindMusic();
            }
            if (instance == null) {
                instance = new ContentResolverFindMusic();
            }
        }
        return instance;
    }

    public static void destory(){
        instance = null;
        cachedSongs.clear();
    }

    public static ArrayList<MusicBean> findSongerIDAllMusic(long artistId){
        ArrayList<MusicBean> musicBeans = new ArrayList<>();
        ContentResolver contentResolver = MusicApplication.getMusicContent().getContentResolver();

        String selection = MediaStore.Audio.Media.ARTIST_ID + "=?";
        String[] selectionArgs = {artistId+""};
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicBean song = mapCursorToMusicBean(cursor);
                if (song != null) {
                    musicBeans.add(song);
                }
            }
            cursor.close();
        }
        return musicBeans;
    }
}
