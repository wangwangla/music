package com.example.learnandroid.constant;

import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.service.MusicControl;
import com.example.learnandroid.utils.SharePerenceUtils;

import java.util.ArrayList;

public class MusicManager {
    private static long id;
    private static int position;
    public static MusicControl musicController;
    private static ArrayList<MusicBean> musicBeans;
    private static boolean isPause = false;

    public static void setData(){
        setData(position);
    }

    public static void setData(int index) {
        if (musicBeans.size()<=0)return;
        isPause = false;
        position = index;
        MusicBean musicBean = musicBeans.get(index);
        MusicManager.setCurrentPlayId(musicBean.getId());
        musicController.setData(musicBean.getPath());
    }

    public static void play() {
        musicController.play();
    }

    public static void pausePlay() {
        isPause = true;
        musicController.pausePlay();           //暂停播放音乐
    }

    public static void continuePlay() {
        if (isPause){
            isPause = false;
            musicController.continuePlay();           //继续播放音乐
        }else {
            setData();
            play();
        }
    }

    public static void seekTo(int progress) {
        musicController.seekTo(progress);//设置音乐的播放位置
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
        setData(index);
        play();
    }

    public static void playPre() {
        if (musicBeans.size() <= 0) return;
        int index = 0;
        if (Constant.playStyle == 0) {
            position--;
            if (position<0) {
                position = musicBeans.size()-1;
            }
            index = position;
        } else {
            int size = musicBeans.size();
            position = (int) Math.random() * (size - 1);
            index = position;
        }
        setData(index);
        play();
    }

    public static ArrayList<MusicBean> getMusicBeans() {
        return musicBeans;
    }

    public static MusicBean getMusicBean() {
        if (musicBeans.size()<=0)return null;
        return musicBeans.get(position);
    }

    public static boolean isPlaying(){
        return musicController.isPlaying();
    }

    public static boolean setInitData() {
        if (musicBeans.size() <= 0) {
            return false;
        }
        int position = SharePerenceUtils.getSharePerenceUtils().histotyPosition();
        setData(position);
        return true;
    }
}
