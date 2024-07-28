package com.example.learnandroid.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.bean.Album;
import com.example.learnandroid.bean.MusicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 19:02
 */
public class AlbumLoader {
    public static ArrayList<MusicBean> getSongsForAlbum(Context context, long albumID) {

        Cursor cursor = makeAlbumSongCursor(context, albumID);
        ArrayList arrayList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                int trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
                String path = cursor.getString(cursor.getColumnIndexOrThrow((MediaStore.Audio.Media.DATA)));
                /*This fixes bug where some track numbers displayed as 100 or 200*/
                while (trackNumber >= 1000) {
                    trackNumber -= 1000; //When error occurs the track numbers have an extra 1000 or 2000 added, so decrease till normal.
                }
                long artistId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
                long albumId = albumID;
//                final MusicBean song = new MusicBean(id, albumId, artistId, title, artist, album, duration, trackNumber,path);
                arrayList.add(new MusicBean(id, albumId, artistId, title, artist, album, duration, trackNumber,path));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static Cursor makeAlbumSongCursor(Context context, long albumID) {
        ContentResolver contentResolver =
                context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String string = "is_music=1 " +
                "AND title != '' " +
                "AND album_id=" + albumID;
        Cursor cursor = contentResolver.query(uri,
                null,
                string,
                null,
                null);
        return cursor;
    }

    public static ArrayList<Album> getArttist(){
        ArrayList<Album> artists = new ArrayList<>();
        Cursor cursor = MusicApplication.getMusicContent().getContentResolver()
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

    public static List<Album> getAllAlbums(Context context) {
        return getAlbumsForCursor(makeAlbumCursor(context, null, null));
    }

    public static Cursor makeAlbumCursor(Context context, String selection, String[] paramArrayOfString) {
        final String albumSortOrder = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER;;
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"},
                        selection, paramArrayOfString, albumSortOrder);

        return cursor;
    }

    public static List<Album> getAlbumsForCursor(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                arrayList.add(new Album(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getLong(3),
                        cursor.getInt(4),
                        cursor.getInt(5)));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }
}
