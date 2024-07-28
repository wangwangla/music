package com.example.learnandroid.data;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.Constant;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/11 16:28
 */
public class SongLoader {
    private static int loadType = 0;
    private static SaoMiaoMusicInterface instance;

    public static void setLoadType(int _loadType) {
        loadType = _loadType;
    }

    public static ArrayList<MusicBean> loadAllSongList() {
        getSaoMiaoMusicInstance().findMusic();
        if (Constant.NO_SET == 0){
            Intent intent = new Intent(Constant.UP_DATE_BOTTOM);
            MusicApplication.getMusicContent().sendBroadcast(intent);
        }
        return getSaoMiaoMusicInstance().getMusicBeans();
    }

    public static MusicBean loadSongById(long songId) {
        String selection = MediaStore.Audio.Media._ID + "=?";
        String[] selectionArgs = new String[] { songId+"" };
        Cursor cursor = MusicApplication.getMusicContent().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
        MusicBean song = null;
        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            int trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
            long artistId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
            long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

            String path = cursor.getString(cursor.getColumnIndexOrThrow((MediaStore.Audio.Media.DATA)));

            song = new MusicBean(id, albumId, artistId, title, artist, album, duration, trackNumber, path);

//            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//            String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            // 其他歌曲信息
            cursor.close();
        }
        return song;
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
    }

    public static ArrayList<MusicBean> findSongerIDAllMusic(long artistId){
        ArrayList<MusicBean> musicBeans = new ArrayList<>();
        ContentResolver contentResolver = MusicApplication.getMusicContent().getContentResolver();
//
//        String[] projection = {
//                MediaStore.Audio.Media._ID,
//                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.ARTIST,
//                MediaStore.Audio.Media.ALBUM
//        };

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
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                int trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
                long artistId1 = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
                long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                String path = cursor.getString(cursor.getColumnIndexOrThrow((MediaStore.Audio.Media.DATA)));

                final MusicBean song = new MusicBean(id, albumId, artistId1, title, artist, album, duration, trackNumber,path);
                musicBeans.add(song);
            }
            cursor.close();
        }
        return musicBeans;
    }
}
