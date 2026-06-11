package com.example.learnandroid.main;

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

import com.example.learnandroid.R;
import com.example.learnandroid.SearchActivity;
import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.application.utils.BitmapUtils;
import com.example.learnandroid.adapter.AlbumSongAdapter;
import com.example.learnandroid.bean.Album;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.data.AlbumLoader;

import java.util.ArrayList;

public class AlbumDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ALBUM_ID = "album_id";

    private long albumId = -1L;
    private ImageView albumBg;
    private TextView albumName;
    private TextView albumInfo;
    private TextView albumEmptyView;
    private RecyclerView albumSongList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_detail);

        albumId = getIntent().getLongExtra(EXTRA_ALBUM_ID, -1L);
        initViews();
        initClicks();
        loadAlbumDetail();
    }

    private void initViews() {
        albumBg = findViewById(R.id.album_bg);
        albumName = findViewById(R.id.album_name);
        albumInfo = findViewById(R.id.album_info);
        albumEmptyView = findViewById(R.id.album_empty_view);
        albumSongList = findViewById(R.id.album_song_list);
        albumSongList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initClicks() {
        View topBack = findViewById(R.id.top_back);
        View albumSearch = findViewById(R.id.album_search);
        topBack.setOnClickListener(v -> finish());
        albumSearch.setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
    }

    private void loadAlbumDetail() {
        Album album = AlbumLoader.getAlbum(this, albumId);
        ArrayList<MusicBean> songs = AlbumLoader.getSongsForAlbum(this, albumId);
        bindAlbum(album, songs);
    }

    private void bindAlbum(@Nullable Album album, @Nullable ArrayList<MusicBean> songs) {
        ArrayList<MusicBean> safeSongs = songs == null ? new ArrayList<>() : songs;
        if (album != null) {
            albumName.setText(album.title);
            albumInfo.setText(getString(R.string.album_detail_meta, album.artistName, safeSongs.size()));
        } else if (!safeSongs.isEmpty()) {
            MusicBean firstSong = safeSongs.get(0);
            albumName.setText(firstSong.getAlbumName());
            albumInfo.setText(getString(R.string.album_detail_meta, firstSong.getArtistName(), safeSongs.size()));
        } else {
            albumName.setText(R.string.albm);
            albumInfo.setText(R.string.album_empty_message);
        }

        Uri albumArtUri = BitmapUtils.getAlbumArtUri(albumId);
        Bitmap bitmap = BitmapUtils.decodeUri(MusicApplication.getMusicContent(), albumArtUri, 800, 800);
        if (bitmap != null) {
            albumBg.setImageBitmap(bitmap);
        } else {
            albumBg.setImageResource(R.mipmap.default_image2);
        }

        if (safeSongs.isEmpty()) {
            albumEmptyView.setVisibility(View.VISIBLE);
            albumSongList.setVisibility(View.GONE);
            albumEmptyView.setText(R.string.album_empty_message);
        } else {
            albumEmptyView.setVisibility(View.GONE);
            albumSongList.setVisibility(View.VISIBLE);
            albumSongList.setAdapter(new AlbumSongAdapter(safeSongs));
        }
    }

}
