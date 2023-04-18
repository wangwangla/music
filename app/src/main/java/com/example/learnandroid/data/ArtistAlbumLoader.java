package com.example.learnandroid.data;

import android.content.Context;

import com.example.learnandroid.bean.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/18 23:30
 */
public class ArtistAlbumLoader {
    public static ArrayList<Album> getAlbumsForArtist(Context context, long artistID) {
        if (artistID == -1)
            return null;
        List<Album> allAlbums = AlbumLoader.getAllAlbums(context);
        ArrayList<Album> artistAlbums = new ArrayList<>();
        for (Album album: allAlbums) {
            if (album.artistId == artistID) {
                artistAlbums.add(album);
            }
        }
        return artistAlbums;
    }
}
