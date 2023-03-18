package com.example.learnandroid.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.learnandroid.data.SaoMiaoMusicInterface;
import com.example.learnandroid.data.SongLoader;

import java.util.ArrayList;

public class SongFragment extends Fragment {
    private ListView songList;
    public SongFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate( R.layout.fragment_song, container,false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.songList = view.findViewById(R.id.songlist);

        initSongList();


        TextView shuffePlayer = view.findViewById(R.id.shuffle_player);
        shuffePlayer.setText("顺序播放");
        View shuffle = view.findViewById(R.id.shuffle_btn);
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index ++;
                if (index>=2){
                    index = 0;
                }
                if (index == 0){
                    shuffePlayer.setText("顺序播放");
                    Constant.playStyle = 0;
                }else if (index == 1){
                    shuffePlayer.setText("随机播放");
                    Constant.playStyle = 1;
                }
            }
        });
    }

    private void initSongList() {
        ArrayList<MusicBean> musicBeans = SongLoader.loadAllSongList();
        MusicManager.setSongList(musicBeans);
        SongAdapter adapter
                = new SongAdapter(
                getContext(),
                R.layout.songlist_view_layout,
                musicBeans
        );
        songList.setAdapter(adapter);
    }

    private int index = 0;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
