package com.example.learnandroid.main;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.learnandroid.CustomTitleActivity;
import com.example.learnandroid.R;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.Constant;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.utils.MusicUtils;
import com.example.learnandroid.utils.TimeUtils;

public class PlayFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate( R.layout.fragment_play, container,false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        ImageView songPlayBtn = view.findViewById(R.id.play_playOrStop);
        if (MusicManager.isPlaying()) {
            songPlayBtn.setImageResource(R.drawable.pause);
        }
        TextView duration = view.findViewById(R.id.play_alltime);
        duration.setText(TimeUtils.longToTime(musicBean.getDuration()));
        songPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MusicManager.musicControl.isPlaying()) {
                    if (MusicManager.musicControl.isCanPlay()) {
                        MusicManager.musicControl.continueMusic();
                        songPlayBtn.setImageResource(R.drawable.pause);
                    }else {
                        MusicManager.musicControl.setData(musicBean.getPath());
                        MusicManager.musicControl.play();
                        songPlayBtn.setImageResource(R.drawable.pause);
                    }
                }else {
                    if (MusicManager.musicControl.isCanPlay()) {
                        MusicManager.musicControl.pausePlay();
                        songPlayBtn.setImageResource(R.drawable.play);
                       }
                }
                Message message = new Message();
                message.what = Constant.UPDATE;
                CustomTitleActivity.conhandler.sendMessage(message);
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
