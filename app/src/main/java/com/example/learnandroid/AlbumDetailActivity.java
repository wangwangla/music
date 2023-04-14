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
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.data.AlbmLoader;
import com.example.learnandroid.utils.BitmapUtils;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/11 23:25
 */
public class AlbumDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_detail);
        Intent intent = getIntent();
        long album_id = intent.getLongExtra("album_id", -1);
        if (album_id != -1L){
            ArrayList<MusicBean> songsForAlbum = AlbmLoader.getSongsForAlbum(this, album_id);
            for (MusicBean musicBean : songsForAlbum) {
                System.out.println(musicBean.toString());
            }
            if (songsForAlbum.size()>0){
                MusicBean musicBean = songsForAlbum.get(0);
                long albumId = musicBean.getAlbumId();
                long artistId = musicBean.getArtistId();
                Uri albumArtUri = BitmapUtils.getAlbumArtUri(artistId);
                ImageView albumBg = findViewById(R.id.album_bg);
                albumBg.setImageURI(albumArtUri);
                TextView albumName = findViewById(R.id.album_name);
                albumName.setText(musicBean.getAlbumName());
                RecyclerView albumSongList = findViewById(R.id.album_song_list);
                albumSongList.setLayoutManager(new LinearLayoutManager(this));
                albumSongList.setAdapter(new AlbumSongAdapter(songsForAlbum));
            }else {

            }
        }
    }
}
