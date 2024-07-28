package com.example.learnandroid.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.bean.Artist;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 18:40
 */
public class ArtistLoader {
    public static ArrayList<Artist> getArttist(){
        ArrayList<Artist> artists = new ArrayList<>();
        Cursor cursor = MusicApplication.getMusicContent().getContentResolver().query(
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

    public static Artist getArtist(Context context, long id) {
        return getArtist(makeArtistCursor(context, "_id=?", new String[]{String.valueOf(id)}));
    }

    public static Artist getArtist(Cursor cursor) {
        Artist artist = new Artist();
        if (cursor != null) {
            if (cursor.moveToFirst())
                artist = new Artist(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
        }
        if (cursor != null)
            cursor.close();
        return artist;
    }


    public static Cursor makeArtistCursor(Context context, String selection, String[] paramArrayOfString) {
        final String artistSortOrder = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER;
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "artist", "number_of_albums", "number_of_tracks"}, selection, paramArrayOfString, artistSortOrder);
        return cursor;
    }


}
