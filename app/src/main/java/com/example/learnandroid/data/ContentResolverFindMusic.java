package com.example.learnandroid.data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;

import com.example.learnandroid.application.MyApplication;
import com.example.learnandroid.bean.MusicBean;

import java.util.ArrayList;

public class ContentResolverFindMusic implements SaoMiaoMusicInterface{
    private ArrayList<MusicBean> musicBeans;
    private Context context;
    public ContentResolverFindMusic(){
        this.context = MyApplication.getMusicContent();
        this.musicBeans = new ArrayList<>();
    }

    @Override
    public synchronized void findMusic() {

        final StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Audio.AudioColumns.IS_MUSIC + "=1");
        selection.append(" AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''");
        selection.append(" AND " + MediaStore.Audio.Media.DATE_ADDED + ">");
        Cursor mCursor = context.getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null, null,
                        null, MediaStore.Audio.AudioColumns.IS_MUSIC);

        musicBeans.clear();
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

    @Override
    public ArrayList<MusicBean> getMusicBeans() {
        return musicBeans;
    }
}
