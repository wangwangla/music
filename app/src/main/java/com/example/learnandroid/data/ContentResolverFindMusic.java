package com.example.learnandroid.data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;

import com.example.learnandroid.MainActivity;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.MusicManager;

import java.util.ArrayList;

public class ContentResolverFindMusic implements SaoMiaoMusicInterface{
    private ArrayList<MusicBean> musicBeans;
    private Context context;
    public ContentResolverFindMusic(Context context){
        this.context = context;
        this.musicBeans = new ArrayList<>();


    }
    @Override
    public void findMusic() {
        if (!requestPermissions()) {
            return;
        }
//        Cursor cursor = context.getContentResolver()
//                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                        null, null, null,
//                        MediaStore.Audio.AudioColumns.IS_MUSIC);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                MusicBean song = new MusicBean();
//                //歌曲名称
//                song.song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
//                //歌手
//                song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
//                //专辑名
//                song.album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
//                //歌曲路径
//                song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
//                //歌曲时长
//                song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
//                //歌曲大小
//                song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
//
//                if (song.size > 1000 * 800) {
//                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
//                    if (song.song.contains("-")) {
//                        String[] str = song.song.split("-");
//                        song.singer = str[0];
//                        song.song = str[1];
//                    }
//                    musicBeans.add(song);
//                }
//            }
//        }

            //four weeks ago
//            long fourWeeksAgo = (System.currentTimeMillis() / 1000) - (4 * 3600 * 24 * 7);
//            long cutoff = PreferencesUtility.getInstance(context).getLastAddedCutoff();
            // use the most recent of the two timestamps
//            if (cutoff < fourWeeksAgo) {
//                cutoff = fourWeeksAgo;
//            }

            final StringBuilder selection = new StringBuilder();
            selection.append(MediaStore.Audio.AudioColumns.IS_MUSIC + "=1");
            selection.append(" AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''");
            selection.append(" AND " + MediaStore.Audio.Media.DATE_ADDED + ">");
//            selection.append(cutoff);
//        SELECT _id, title, artist, album, duration, track, artist_id, album_id

        Cursor mCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        musicBeans = new ArrayList<>();
        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                long id = mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String title = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                int duration = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                int trackNumber = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
                long artistId = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
                long albumId = mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                String path = mCursor.getString(mCursor.getColumnIndexOrThrow((MediaStore.Audio.Media.DATA)));

                final MusicBean song = new MusicBean(id, albumId, artistId, title, artist, album, duration, trackNumber,path);

                musicBeans.add(song);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
    }
//            // 释放资源
//            cursor.close();
//        }

    public boolean requestPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && context.checkSelfPermission
                (Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//请求权
            ((Activity)(context)).requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<MusicBean> getMusicBeans() {
        return musicBeans;
    }
}
