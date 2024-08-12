package com.example.learnandroid.main;

import android.app.Activity;
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
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.data.AlbumLoader;
import com.example.learnandroid.application.utils.BitmapUtils;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/9 20:13
 */
public class AlbumDetailFragment extends Fragment {
    private static AppCompatActivity activity;
    public static Fragment newInstance(Activity context, long albumID, boolean b) {
        activity = (AppCompatActivity)context;
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle args = new Bundle();
        args.putLong("album_id", albumID);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.album_detail,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long albumID = getArguments().getLong("album_id");
        View topBack = view.findViewById(R.id.top_back);
        topBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.remove(AlbumDetailFragment.this);
                transaction.commit();
            }
        });
        if (albumID != -1L){
            ArrayList<MusicBean> songsForAlbum = AlbumLoader.getSongsForAlbum(getContext(), albumID);
            if (songsForAlbum.size()>0){
                MusicBean musicBean = songsForAlbum.get(0);
                Uri albumArtUri = BitmapUtils.getAlbumArtUri(albumID);
                ImageView albumBg = view.findViewById(R.id.album_bg);
                albumBg.setImageURI(albumArtUri);

                TextView albumName = view.findViewById(R.id.album_name);
                albumName.setText(musicBean.getAlbumName());
                RecyclerView albumSongList = view.findViewById(R.id.album_song_list);
                albumSongList.setLayoutManager(new LinearLayoutManager(getContext()));
                albumSongList.setAdapter(new AlbumSongAdapter(songsForAlbum));
            }
        }
    }
}
