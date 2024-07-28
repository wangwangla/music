package com.example.learnandroid.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnandroid.R;
import com.example.learnandroid.adapter.AlbumSongAdapter;
import com.example.learnandroid.adapter.ArtistAlbumAdpater;
import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.bean.Album;
import com.example.learnandroid.bean.Artist;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.data.AlbumLoader;
import com.example.learnandroid.data.ArtistAlbumLoader;
import com.example.learnandroid.data.ArtistLoader;
import com.example.learnandroid.data.SongLoader;
import com.example.learnandroid.sign.SignListener;
import com.example.learnandroid.utils.BitmapUtils;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/20 23:33
 */
public class ArtistDetailFragment extends Fragment {
    private static AppCompatActivity activity;
    public static Fragment newInstance(Activity context, long artistID, boolean b) {
        activity = (AppCompatActivity)context;
        ArtistDetailFragment fragment = new ArtistDetailFragment();
        Bundle args = new Bundle();
        args.putLong("artistID", artistID);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_artist_detail,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long artistID = getArguments().getLong("artistID");
        Artist artist = ArtistLoader.getArtist(activity, artistID);

        TextView artistName = view.findViewById(R.id.artist_name);
        artistName.setText(artist.name);

        RecyclerView artistAlbumList = view.findViewById(R.id.artist_album_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        artistAlbumList.setLayoutManager(linearLayoutManager);
        artistAlbumList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                //设置距离为20px
                outRect.left = 10;
            }
        });
        ArrayList<Album> albumsForArtist = ArtistAlbumLoader.getAlbumsForArtist(activity, artistID);
        ImageView artistBg = view.findViewById(R.id.artist_bg);
        Album album = albumsForArtist.get(0);
        Uri albumArtUri = BitmapUtils.getAlbumArtUri(album.id);
        Bitmap bitmap = BitmapUtils.decodeUri(MusicApplication.getMusicContent(),albumArtUri,300,300);
        if(bitmap!=null) {
            artistBg.setImageBitmap(bitmap);
        }
        ArtistAlbumAdpater artistAlbumAdpater = new ArtistAlbumAdpater(albumsForArtist);
        artistAlbumList.setAdapter(artistAlbumAdpater);
//        artistAlbumList.addItemDecoration(new SpaceItemDecoration(getContext(),30));
        RecyclerView artistAlbumSongList = view.findViewById(R.id.artist_album_song_list);
        artistAlbumSongList.setLayoutManager(new LinearLayoutManager(activity));
//        Album album = albumsForArtist.get(0);
//
//        ArrayList<MusicBean> songsForAlbum = AlbumLoader.getSongsForAlbum(activity, album.id);
//        artistAlbumSongList.setAdapter(new AlbumSongAdapter(songsForAlbum));

        artistAlbumAdpater.setCallBackListener(new SignListener(){
            @Override
            public void sign(Object o) {
                long o1 = (long) (o);
                ArrayList<MusicBean> songsForAlbum = AlbumLoader.getSongsForAlbum(activity, o1);
                artistAlbumSongList.setAdapter(new AlbumSongAdapter(songsForAlbum));
            }
        });
        ArrayList<MusicBean> songerIDAllMusic = SongLoader.findSongerIDAllMusic(artistID);
        artistAlbumSongList.setAdapter(new AlbumSongAdapter(songerIDAllMusic));
        View artistDetailBack = view.findViewById(R.id.artist_detail_back);
        artistDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.remove(ArtistDetailFragment.this);
                transaction.commit();
            }
        });

    }
}
