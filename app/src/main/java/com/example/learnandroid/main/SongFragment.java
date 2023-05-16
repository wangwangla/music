package com.example.learnandroid.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.learnandroid.R;
import com.example.learnandroid.adapter.SongAdapter;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.Constant;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.data.SongLoader;

import java.util.ArrayList;

public class SongFragment extends Fragment {
    private View rootView;
    private ListView songList;
    private ArrayList<MusicBean> musicBeans;
    private SongAdapter adapter;
    private ImageView shuffleIcon;
    private int playmode = 0;

    private View.OnClickListener shuffleController = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            playmode++;
            if (playmode >=2){
                playmode = 0;
            }
            updateShuffleMode();
        }
    };

    private void updateShuffleMode() {
        if (playmode == 0){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.repeat_all);
            shuffleIcon.setImageBitmap(bitmap);
            Constant.playStyle = 0;
        }else if (playmode == 1){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shuffle);
            shuffleIcon.setImageBitmap(bitmap);
            Constant.playStyle = 1;
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate( R.layout.fragment_song, container,false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootView = view;
        new LoadingAsset().execute("");
        songList = rootView.findViewById(R.id.songlist);
        shuffleIcon = view.findViewById(R.id.shuffle_btn);
        shuffleIcon.setOnClickListener(shuffleController);
        updateShuffleMode();
    }

    private void initSongList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getActivity().checkSelfPermission
                (Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        }
        musicBeans = SongLoader.loadAllSongList();
        MusicManager.setSongList(musicBeans);
        adapter = new SongAdapter(getContext(), R.layout.songlist_view_layout, musicBeans);
    }


    public class LoadingAsset extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            initSongList();
            return "Ex";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            songList.setAdapter(adapter);
            TextView songNum = rootView.findViewById(R.id.song_num);
            songNum.setText(adapter.getCount()+" songs");
        }
    }
}
