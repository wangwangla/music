package com.example.learnandroid.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.learnandroid.R;
import com.example.learnandroid.adapter.SongAdapter;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.Constant;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.data.ContentResolverFindMusic;
import com.example.learnandroid.data.SongLoader;

import java.util.ArrayList;

public class PlayerOrderFragment extends Fragment {

    public PlayerOrderFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate( R.layout.fragment_playorder, container,false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<MusicBean> musicBeans = SongLoader.loadAllSongList();
        ListView songList = view.findViewById(R.id.playlist);
        SongAdapter adapter
                = new SongAdapter(
                getContext(),
                R.layout.songlist_view_layout,
                musicBeans
        );
        MusicManager.setSongList(musicBeans);
        songList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
