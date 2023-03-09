package com.example.learnandroid.main;

import android.os.Bundle;
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

import java.util.ArrayList;

public class SongFragment extends Fragment {
    private SaoMiaoMusicInterface saoMiaoMusicInterface;
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
        saoMiaoMusicInterface = Constant.contentResolverFindMusic;
        saoMiaoMusicInterface.findMusic();
        ArrayList<MusicBean> musicBeans = saoMiaoMusicInterface.getMusicBeans();
        MusicManager.setSongList(musicBeans);

        ListView songList = view.findViewById(R.id.songlist);
        SongAdapter adapter
                = new SongAdapter(
                        getContext(),
                        R.layout.songlist_view_layout,
                musicBeans
                );
        songList.setAdapter(adapter);
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

    private int index = 0;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        saoMiaoMusicInterface.findMusic();
        ArrayList<MusicBean> musicBeans = saoMiaoMusicInterface.getMusicBeans();
        MusicManager.setSongList(musicBeans);
    }
}
