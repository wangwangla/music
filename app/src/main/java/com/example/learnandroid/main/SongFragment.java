package com.example.learnandroid.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.learnandroid.R;
import com.example.learnandroid.SongAdapter;
import com.example.learnandroid.bean.MusicBean;
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
        saoMiaoMusicInterface = new ContentResolverFindMusic(getContext());
        saoMiaoMusicInterface.findMusic();
        ArrayList<MusicBean> musicBeans = saoMiaoMusicInterface.getMusicBeans();
        for (MusicBean musicBean : musicBeans) {
            System.out.println(musicBean.toString());
        }
        ListView songList = view.findViewById(R.id.songlist);
//        ArrayAdapter<String> adapter
//                =new ArrayAdapter<>(
//                getContext()
//                ,android.R.layout.simple_list_item_1,data);
        SongAdapter adapter
                = new SongAdapter(
                        getContext(),
                        R.layout.songlist_view_layout,
                musicBeans
                );
        songList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
