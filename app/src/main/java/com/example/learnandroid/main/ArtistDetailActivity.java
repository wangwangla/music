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
import com.example.learnandroid.adapter.AlbumSongAdapter;
import com.example.learnandroid.adapter.ArtistAlbumAdpater;
import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.application.utils.BitmapUtils;
import com.example.learnandroid.bean.Album;
import com.example.learnandroid.bean.Artist;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.data.AlbumLoader;
import com.example.learnandroid.data.ArtistAlbumLoader;
import com.example.learnandroid.data.ArtistLoader;
import com.example.learnandroid.data.SongLoader;

import java.util.ArrayList;

public class ArtistDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ARTIST_ID = "artist_id";

    private long artistId = -1L;
    private ImageView artistBg;
    private TextView artistName;
    private TextView artistInfo;
    private TextView artistEmptyView;
    private RecyclerView artistAlbumList;
    private RecyclerView artistAlbumSongList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        artistId = getIntent().getLongExtra(EXTRA_ARTIST_ID, -1L);
        initViews();
        initClicks();
        loadArtistDetail();
    }

    private void initViews() {
        artistBg = findViewById(R.id.artist_bg);
        artistName = findViewById(R.id.artist_name);
        artistInfo = findViewById(R.id.artist_info);
        artistEmptyView = findViewById(R.id.artist_empty_view);
        artistAlbumList = findViewById(R.id.artist_album_list);
        artistAlbumSongList = findViewById(R.id.artist_album_song_list);

        LinearLayoutManager albumLayoutManager = new LinearLayoutManager(this);
        albumLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        artistAlbumList.setLayoutManager(albumLayoutManager);
        artistAlbumSongList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initClicks() {
        View backView = findViewById(R.id.artist_detail_back);
        View searchView = findViewById(R.id.album_search);
        backView.setOnClickListener(v -> finish());
        searchView.setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
    }

    private void loadArtistDetail() {
        Artist artist = ArtistLoader.getArtist(this, artistId);
        ArrayList<Album> artistAlbums = ArtistAlbumLoader.getAlbumsForArtist(this, artistId);
        ArrayList<MusicBean> artistSongs = SongLoader.findSongerIDAllMusic(artistId);
        bindArtist(artist, artistAlbums, artistSongs);
    }

    private void bindArtist(@Nullable Artist artist, @Nullable ArrayList<Album> albums, @Nullable ArrayList<MusicBean> songs) {
        ArrayList<Album> safeAlbums = albums == null ? new ArrayList<>() : albums;
        ArrayList<MusicBean> safeSongs = songs == null ? new ArrayList<>() : songs;

        String displayName = artist != null && artist.id != -1 && artist.name != null && !artist.name.trim().isEmpty()
                ? artist.name
                : getString(R.string.songer);
        artistName.setText(displayName);

        int albumCount = artist != null && artist.albumCount >= 0 ? artist.albumCount : safeAlbums.size();
        int songCount = artist != null && artist.songCount >= 0 ? artist.songCount : safeSongs.size();
        artistInfo.setText(getString(R.string.artist_detail_meta, albumCount, songCount));

        bindHeaderArt(safeAlbums, safeSongs);
        bindAlbums(safeAlbums);
        bindSongs(safeSongs, safeSongs.isEmpty() ? R.string.artist_empty_message : 0);
    }

    private void bindHeaderArt(ArrayList<Album> albums, ArrayList<MusicBean> songs) {
        long artAlbumId = -1L;
        if (!albums.isEmpty()) {
            artAlbumId = albums.get(0).id;
        } else if (!songs.isEmpty()) {
            artAlbumId = songs.get(0).getAlbumId();
        }

        if (artAlbumId == -1L) {
            artistBg.setImageResource(R.mipmap.default_image2);
            return;
        }

        Uri albumArtUri = BitmapUtils.getAlbumArtUri(artAlbumId);
        Bitmap bitmap = BitmapUtils.decodeUri(MusicApplication.getMusicContent(), albumArtUri, 800, 800);
        if (bitmap != null) {
            artistBg.setImageBitmap(bitmap);
        } else {
            artistBg.setImageResource(R.mipmap.default_image2);
        }
    }

    private void bindAlbums(ArrayList<Album> albums) {
        ArtistAlbumAdpater adapter = new ArtistAlbumAdpater(albums);
        adapter.setCallBackListener(o -> {
            long albumId = (long) o;
            ArrayList<MusicBean> songsForAlbum = AlbumLoader.getSongsForAlbum(this, albumId);
            bindSongs(songsForAlbum, R.string.artist_album_empty_message);
        });
        artistAlbumList.setAdapter(adapter);
        artistAlbumList.setVisibility(albums.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void bindSongs(@Nullable ArrayList<MusicBean> songs, int emptyMessageRes) {
        ArrayList<MusicBean> safeSongs = songs == null ? new ArrayList<>() : songs;
        if (safeSongs.isEmpty()) {
            artistEmptyView.setVisibility(View.VISIBLE);
            artistAlbumSongList.setVisibility(View.GONE);
            artistEmptyView.setText(emptyMessageRes == 0 ? R.string.artist_empty_message : emptyMessageRes);
        } else {
            artistEmptyView.setVisibility(View.GONE);
            artistAlbumSongList.setVisibility(View.VISIBLE);
            artistAlbumSongList.setAdapter(new AlbumSongAdapter(safeSongs));
        }
    }
}
