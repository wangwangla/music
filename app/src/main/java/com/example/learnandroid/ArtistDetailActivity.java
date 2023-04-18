package com.example.learnandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnandroid.adapter.ArtistAlbumAdpater;
import com.example.learnandroid.bean.Artist;
import com.example.learnandroid.data.ArtistLoader;

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
        TextView artistName = findViewById(R.id.artist_name);
        artistName.setText(artist.name);
        RecyclerView artistAlbumList = findViewById(R.id.artist_album_list);
        artistAlbumList.setLayoutManager(new LinearLayoutManager(this));
        artistAlbumList.setAdapter(new ArtistAlbumAdpater(artist));
    }
}
