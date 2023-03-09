package com.example.learnandroid.constant;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.learnandroid.CustomTitleActivity;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.data.ContentResolverFindMusic;
import com.example.learnandroid.service.MusicControl;

import java.util.ArrayList;

public class MusicManager {
    private static long id;
    public static MusicControl musicControl;
    private static int position;
    private static ArrayList<MusicBean> musicBeans;

    public static void setData(int index) {
        position = index;
        MusicBean musicBean = musicBeans.get(index);
        musicControl.setData(musicBean.getPath());
    }

    public static void play() {
        musicControl.play();

        Message message = new Message();
        message.what = Constant.PLAY;
        message.arg1 = position;
        CustomTitleActivity.conhandler.sendMessage(message);
    }

    public static void pausePlay() {
        musicControl.pausePlay();           //暂停播放音乐
    }

    public static void continuePlay() {
        musicControl.continuePlay();           //继续播放音乐
    }

    public static void seekTo(int progress) {
        musicControl.seekTo(progress);//设置音乐的播放位置
    }

    public static void setCurrentPlayId(long _id) {
        id = _id;
    }

    public static long getId() {
        return id;
    }

    public static void setIndex(int _position) {
        position = _position;
    }

    public static int getPosition() {
        return position;
    }

    public static void setSongList(ArrayList<MusicBean> _musicBeans) {
        musicBeans = _musicBeans;
    }

    public static void playNext() {
        if (musicBeans.size() <= 0) return;
        MusicBean musicBean = null;
        int index = 0;
        if (Constant.playStyle == 0) {
            position++;
            if (musicBeans.size() <= position) {
                position = 0;
            }
            index = position;
        } else {
            int size = musicBeans.size();
            position = (int) Math.random() * (size - 1);
            index = position;
        }
        musicBean = musicBeans.get(index);
        setData(index);
        play();
    }

    public static ArrayList<MusicBean> getMusicBeans() {
        return musicBeans;
    }

    public static MusicBean getMusicBean() {
        if (musicBeans.size()<0)return null;
        return musicBeans.get(position);
    }

    public static boolean isPlaying(){
        return musicControl.isPlaying();
    }

    public static void playAndSetData() {

    }
}
