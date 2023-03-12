package com.example.learnandroid.data;

import android.database.Cursor;
import android.provider.MediaStore;

import com.example.learnandroid.application.MyApplication;
import com.example.learnandroid.bean.Album;
import com.example.learnandroid.bean.Artist;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 19:02
 */
public class AlbmLoader {
    public static ArrayList<Album> getArttist(){
        ArrayList<Album> artists = new ArrayList<>();
        Cursor cursor = MyApplication.getMusicContent().getContentResolver()
                .query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"},
                        null, null, null);
        if (cursor!=null){
            if (cursor.moveToFirst()) {
                do{
                    artists.add(
                            new Album(
                                    cursor.getLong(0),
                                    cursor.getString(1),
                                    cursor.getString(2),
                                    cursor.getLong(3),
                                    cursor.getInt(4),
                                    cursor.getInt(5)));
                }while (cursor.moveToNext());
            }
        }
        return artists;
    }
}
