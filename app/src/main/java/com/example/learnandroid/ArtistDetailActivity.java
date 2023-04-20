package com.example.learnandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnandroid.adapter.AlbumSongAdapter;
import com.example.learnandroid.adapter.ArtistAlbumAdpater;
import com.example.learnandroid.application.MyApplication;
import com.example.learnandroid.bean.Album;
import com.example.learnandroid.bean.Artist;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.data.AlbumLoader;
import com.example.learnandroid.data.ArtistAlbumLoader;
import com.example.learnandroid.data.ArtistLoader;
import com.example.learnandroid.sign.SignListener;
import com.example.learnandroid.utils.BitmapUtils;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/13 23:15
 */
public class ArtistDetailActivity extends AppCompatActivity {
    private ImageView artistBg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        long artistID = intent.getLongExtra("artistID",1);
        Artist artist = ArtistLoader.getArtist(this, artistID);
        setContentView(R.layout.activity_artist_detail);
        artistBg = findViewById(R.id.artist_bg);

        Uri albumArtUri = BitmapUtils.getAlbumArtUri(artistID);
        Bitmap bitmap = BitmapUtils.decodeUri(MyApplication.getMusicContent(),albumArtUri,300,300);
        artistBg.setImageBitmap(bitmap);

        TextView artistName = findViewById(R.id.artist_name);
        artistName.setText(artist.name);
        RecyclerView artistAlbumList = findViewById(R.id.artist_album_list);
        artistAlbumList.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Album> albumsForArtist = ArtistAlbumLoader.getAlbumsForArtist(this, artistID);
        ArtistAlbumAdpater artistAlbumAdpater = new ArtistAlbumAdpater(albumsForArtist);
        artistAlbumList.setAdapter(artistAlbumAdpater);

        RecyclerView artistAlbumSongList = findViewById(R.id.artist_album_song_list);
        artistAlbumSongList.setLayoutManager(new LinearLayoutManager(this));
        Album album = albumsForArtist.get(0);

        ArrayList<MusicBean> songsForAlbum = AlbumLoader.getSongsForAlbum(this, album.id);
        artistAlbumSongList.setAdapter(new AlbumSongAdapter(songsForAlbum));

        artistAlbumAdpater.setCallBackListener(new SignListener(){
            @Override
            public void sign(Object o) {
                long o1 = (long) (o);
                ArrayList<MusicBean> songsForAlbum = AlbumLoader.getSongsForAlbum(ArtistDetailActivity.this, o1);
                artistAlbumSongList.setAdapter(new AlbumSongAdapter(songsForAlbum));
            }
        });

    }
}
