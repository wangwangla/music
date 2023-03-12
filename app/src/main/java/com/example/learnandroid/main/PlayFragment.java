package com.example.learnandroid.main;

import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;

import com.example.learnandroid.PlayActivity;
import com.example.learnandroid.R;
import com.example.learnandroid.application.MyApplication;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.utils.MusicUtils;
import com.example.learnandroid.utils.TimeUtils;

public class PlayFragment extends Fragment {
    private TextView playTime;
    private SeekBar playProcess;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateBottomView();
        }
    };

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            upateDate();
        }
    };

    public void upateDate(){
        try {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    playTime.setText(TimeUtils.longToTime(MusicManager.getCurrentPosition()));
                    playProcess.setProgress(TimeUtils.miao(MusicManager.getCurrentPosition()));
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate( R.layout.fragment_play, container,false);
        return view;
    }

    private View view;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        MusicManager.addUpdateView(runnable);
        MusicManager.addTimeView(timeRunnable);
        playTime = view.findViewById(R.id.play_time);
        playProcess = view.findViewById(R.id.play_sb);
        ImageView songPlayBtn = view.findViewById(R.id.play_playOrStop);
        if (MusicManager.isPlaying()) {
            songPlayBtn.setImageResource(R.drawable.pause);
        }
        songPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicManager.isPlaying()) {
                    MusicManager.pausePlay();
                }else {
                    MusicManager.continuePlay();
                }
            }
        });
        View playPre = view.findViewById(R.id.play_pre);
        playPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playPre();
            }
        });
        view.findViewById(R.id.play_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playNext();
            }
        });
        updateBottomView();
    }

    private void updateBottomView() {
        ImageView ivMusic = view.findViewById(R.id.play_albm_pic);
        MusicBean musicBean = MusicManager.getMusicBean();
        if (musicBean==null)return;
        Uri albumArtUri = MusicUtils.getAlbumArtUri(musicBean.getAlbumId());
        Bitmap bitmap = MusicUtils.decodeUri(getContext(),albumArtUri,300,300);
        if (bitmap!=null) {
            ivMusic.setImageBitmap(bitmap);
        }
        TextView songName = view.findViewById(R.id.play_song_name);
        songName.setText(musicBean.getTitle());
        TextView songSonger = view.findViewById(R.id.play_song_namer);
        songSonger.setText(musicBean.getArtistName());
        playProcess.setMax(TimeUtils.miao(musicBean.getDuration()));
        TextView duration = view.findViewById(R.id.play_alltime);
        duration.setText(TimeUtils.longToTime(MusicManager.getDuration()));

        ImageView songPlayBtn = view.findViewById(R.id.play_playOrStop);
        if (MusicManager.isPlaying()) {
            songPlayBtn.setImageResource(R.drawable.pause);
        }else {
            songPlayBtn.setImageResource(R.drawable.play);
        }
    }

    @Override
    public void onDestroyView() {
        MusicManager.removeRunnable(timeRunnable);
        MusicManager.removeRunnable(runnable);
        super.onDestroyView();
    }
}
