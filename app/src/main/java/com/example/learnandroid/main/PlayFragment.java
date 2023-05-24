package com.example.learnandroid.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.learnandroid.MusicMainActivity;
import com.example.learnandroid.PlayActivity;
import com.example.learnandroid.R;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.navutil.NavigationUtils;
import com.example.learnandroid.utils.BitmapUtils;
import com.example.learnandroid.utils.TimeUtils;

public class PlayFragment extends Fragment {
    private TextView playTime;
    private SeekBar playProcess;
    private MusicBean currentMusicBean;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateLrcView();
        }
    };

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            upateDate();
        }
    };

    private PlayActivity playActivity;
    public PlayFragment(PlayActivity playActivity) {
        this.playActivity = playActivity;
    }

    public void upateDate(){
        try {
            if (getActivity()==null){
                MusicManager.removeTimeRunnable(timeRunnable);
                return;
            }
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
        RelativeLayout playTop = view.findViewById(R.id.play_top);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) playTop.getLayoutParams();
        //距离上下左右
        layoutParams.setMargins(0, getStatusBarHeight(playActivity), 0, 0);
        playTop.setLayoutParams(layoutParams);
        View topBack = playTop.findViewById(R.id.top_back);
        topBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(playActivity, MusicMainActivity.class);
                playActivity.startActivity(intent);
            }
        });
        TextView songName = playTop.findViewById(R.id.song_name);
        songName.setTextColor(Color.BLACK);
        ImageView back = playTop.findViewById(R.id.top_back);
        back.setColorFilter(Color.BLACK);

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
        updateLrcView();
        playProcess.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float v = progress * 1.0f / playProcess.getMax();
                    float v1 = v * MusicManager.getDuration();
                    MusicManager.seekTo((int) v1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private void updateLrcView() {
        MusicBean musicBean = MusicManager.getMusicBean();
        ImageView ivMusic = view.findViewById(R.id.play_albm_pic);
        ivMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor();
                NavigationUtils.navigateToGeci(playActivity, (int) musicBean.getId());
            }
        });
        if (musicBean==null)return;

        if (currentMusicBean != null){
            if (currentMusicBean.getId() == musicBean.getId()){
                ImageView songPlayBtn = view.findViewById(R.id.play_playOrStop);
                if (MusicManager.isPlaying()) {
                    songPlayBtn.setImageResource(R.drawable.pause);
                }else {
                    songPlayBtn.setImageResource(R.drawable.play);
                }
                return;
            }
        }
        currentMusicBean = musicBean;

        Uri albumArtUri = BitmapUtils.getAlbumArtUri(musicBean.getAlbumId());
        System.out.println(musicBean.getAlbumId()+"-===========================================");
        Bitmap bitmap = BitmapUtils.decodeUri(getContext(),albumArtUri,300,300);
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

    private void changeColor() {
        ImageView imageView = playActivity.findViewById(R.id.top_back);
        imageView.setColorFilter(Color.BLACK);
        TextView allSongName = playActivity.findViewById(R.id.song_name);
        allSongName.setTextColor(Color.BLACK);
    }

    @Override
    public void onDestroyView() {
        MusicManager.removeRunnable(timeRunnable);
        MusicManager.removeRunnable(runnable);
        super.onDestroyView();
    }
}
