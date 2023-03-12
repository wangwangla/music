package com.example.learnandroid.data;

import android.database.Cursor;
import android.provider.MediaStore;

import com.example.learnandroid.adapter.ArtistAdapter;
import com.example.learnandroid.application.MyApplication;
import com.example.learnandroid.bean.Artist;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 18:40
 */
public class ArtistLoader {
    public static ArrayList<Artist> getArttist(){
        ArrayList<Artist> artists = new ArrayList<>();
        Cursor cursor = MyApplication.getMusicContent().getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "artist", "number_of_albums", "number_of_tracks"},
                null, null, null);
        if (cursor!=null){
            if (cursor.moveToFirst()) {
                do{
                    artists.add(new Artist(cursor.getLong(0),
                            cursor.getString(1), cursor.getInt(2),
                            cursor.getInt(3)));
                }while (cursor.moveToNext());
            }
        }
        return artists;
    }
}
