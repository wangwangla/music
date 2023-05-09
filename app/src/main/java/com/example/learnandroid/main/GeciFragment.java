package com.example.learnandroid.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.learnandroid.R;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.data.DefaultLrcParser;
import com.example.learnandroid.data.LrcMusic;
import com.example.learnandroid.data.LrcRow;
import com.example.learnandroid.data.SongLoader;
import com.example.learnandroid.utils.BitmapUtils;
import com.example.learnandroid.utils.Utils;
import com.example.learnandroid.view.ShowLrcView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GeciFragment extends Fragment {
    private ShowLrcView showLrcView;
    private static AppCompatActivity activity;
    private View rootView;
    private Runnable updateLrc = new Runnable() {
        @Override
        public void run() {
            MusicBean musicBean = MusicManager.getMusicBean();
            extracted(musicBean.getId());
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLrcView.seekTo((int) MusicManager.getCurrentPosition(),false,false);
                }
            });
        }
    };

    public static Fragment newInstance(Activity context, long musicId, boolean b) {
        activity = (AppCompatActivity)context;
        GeciFragment fragment = new GeciFragment();
        Bundle args = new Bundle();
        args.putLong("musicId", musicId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_geci, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootView = view;
        Bundle arguments = getArguments();
        long songID = arguments.getLong("musicId");
        System.out.println(songID +" -------------------- songId");
        extracted(songID);
    }

    private void extracted(long songID) {
        ImageView songPic = rootView.findViewById(R.id.lrcy_pic);
        MusicBean musicBean = SongLoader.loadSongById(songID);
        if (musicBean==null)return;
        String path = musicBean.getPath();
        System.out.println(path+"--------------------------------------------");
        ///storage/emulated/0/PMSLLM/Music/周杰伦 - 晴天.mp3
        String path1 = path(path);

        Uri albumArtUri = BitmapUtils.getAlbumArtUri(musicBean.getAlbumId());
        System.out.println(musicBean.getArtistId());
        Bitmap bitmap = BitmapUtils.decodeUri(getContext(),albumArtUri,300,300);
        if (bitmap!=null) {
            songPic.setImageBitmap(bitmap);
        }

        try {
            InputStream fileInputStream = new FileInputStream(new File(path1));
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder builder = new StringBuilder();
            String s = null;
            while (true) {
                try {
                    if (!((s = br.readLine()) != null)) break;
                    if (!TextUtils.isEmpty(s)) {
                        builder.append(s);
                        builder.append("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            showLrcView = rootView.findViewById(R.id.view);
            List<LrcRow> lrcRows = DefaultLrcParser.getIstance().getLrcRows(builder.toString());
            showLrcView.setLrcRows(lrcRows);
            showLrcView.seekTo((int) MusicManager.getCurrentPosition(),false,false);
            MusicManager.addTimeView(runnable);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    public String path (String songPath){
        File file = new File(songPath);
        String parentPath = file.getParent();
//        \storage\emulated\0\PMSLLM\Music
        System.out.println("parent : "+ parentPath);
        String songName = file.getName();
        System.out.println(songName);
        int lastIndexPoint = songName.lastIndexOf(".");
        String substring = songName.substring(0, lastIndexPoint);
        String lrcPath = parentPath + "/" + substring + ".lrc";
        System.out.println(lrcPath);
        return lrcPath;
    }
}