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

import java.util.ArrayList;

public class PlayerOrderFragment extends Fragment {

    private Handler handler;
    public PlayerOrderFragment(Handler runnable) {
        this.handler = runnable;
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

        ArrayList<MusicBean> musicBeans = new ArrayList<>();
        ListView songList = view.findViewById(R.id.playlist);
        SongAdapter adapter
                = new SongAdapter(
                getContext(),
                R.layout.songlist_view_layout,
                musicBeans
        );
        MusicManager.setSongList(musicBeans);
        songList.setAdapter(adapter);
//        TextView shuffePlayer = view.findViewById(R.id.shuffle_player);
//        shuffePlayer.setText("顺序播放");
//        View shuffle = view.findViewById(R.id.shuffle_btn);
//        shuffle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                index ++;
//                if (index>=2){
//                    index = 0;
//                }
//                if (index == 0){
//                    shuffePlayer.setText("顺序播放");
//                    Constant.playStyle = 0;
//                }else if (index == 1){
//                    shuffePlayer.setText("随机播放");
//                    Constant.playStyle = 1;
//                }
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
