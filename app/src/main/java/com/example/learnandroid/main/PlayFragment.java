package com.example.learnandroid.main;

import android.content.Context;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
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
            if (getActivity()==null){
                MusicManager.removeTimeRunnable();
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
        playProcess.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    System.out.println(progress);
                    float v = progress * 1.0f / playProcess.getMax();
                    float v1 = v * MusicManager.getDuration();
                    System.out.println(v1);
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
        ImageView bgAlbm = view.findViewById(R.id.bg_albm);
        Bitmap bitmap1 = MusicUtils.decodeUri(getContext(),albumArtUri);
        if (bitmap!=null) {
            bgAlbm.setImageBitmap(blurBitmap(getContext(),bitmap1));
        }
    }

    private static final float BITMAP_SCALE = 0.009f;
    /** * 模糊图片的具体方法 * * @param context 上下文对象 * @param image 需要模糊的图片 * @return 模糊处理后的图片 */
    public static Bitmap blurBitmap(Context context, Bitmap image) {
        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);
        // 将缩小后的图片做为预渲染的图片
         Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象 ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间 // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去 Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        // 设置渲染的模糊程度, 25f是最大模糊度 blurScript.setRadius(blurRadius);
        // 设置blurScript对象的输入内存 blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中 blurScript.forEach(tmpOut);
        // 将数据填充到Allocation中 tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    @Override
    public void onDestroyView() {
        MusicManager.removeRunnable(timeRunnable);
        MusicManager.removeRunnable(runnable);
        super.onDestroyView();
    }
}
