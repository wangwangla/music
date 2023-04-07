package com.example.learnandroid.constant;

import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.service.MusicControl;
import com.example.learnandroid.utils.SharePerenceUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MusicManager {
    private static long id = -1;
    private static int position = 0;
    public static MusicControl musicController;
    private static ArrayList<MusicBean> musicBeans;
    private static boolean isPause = false;
    private static ArrayList<Runnable> arrayList = new ArrayList<>();

    public static void addUpdateView(Runnable runnable){
        arrayList.add(runnable);
    }

    public static void removeRunnable(Runnable runnable){
        arrayList.remove(runnable);
    }

    public static void setData(){
        setData(position);
    }

    public static void updateListener(){
        for (Runnable runnable : arrayList) {
            runnable.run();
        }
    }

    public static void setData(int index) {
        if (musicBeans.size()<=0)return;
        MusicBean musicBean = musicBeans.get(index);
        if (id == musicBean.getId())return;
        isPause = false;
        position = index;
        setCurrentPlayId(musicBean.getId());
        musicController.setData(musicBean.getPath());
        updateListener();
    }

    public static long getCurrentPosition(){
        return musicController.getCurrentPosition();
    }

    public static void play() {
        addTimer();
        musicController.play();
        updateListener();
    }

    public static void pausePlay() {
        isPause = true;
        musicController.pausePlay();           //暂停播放音乐
        updateListener();
    }

    public static void continuePlay() {
        if (isPause){
            isPause = false;
            musicController.continuePlay();           //继续播放音乐
        }else {
            setData();
            play();
        }
        updateListener();
        addTimer();
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
        setDataAndPlay(index);
    }

    private static void setDataAndPlay(int index) {
        setData(index);
        play();
        updateListener();
        addTimer();
    }

    public static void playPre() {
        if (musicBeans.size() <= 0) return;
        int index = 0;
        if (Constant.playStyle == Constant.playOrder) {
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
        setDataAndPlay(index);
    }

    public static MusicBean getMusicBean() {
        if (musicBeans.size()<=0)return null;
        return musicBeans.get(position);
    }

    public static boolean isPlaying(){
        return musicController.isPlaying();
    }

    public static long getDuration() {
        return musicBeans.get(position).getDuration();
    }

    private static Timer timer;

    public static void addTimer() {        //添加计时器用于设置音乐播放器中的播放进度条
        if (timer!=null) {
            timer.cancel();
            timer = null;
        }
        if (timer == null) {
            timer = new Timer();     //创建计时器对象
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                if (timeRunnable==null)return;
                for (Runnable runnable : timeRunnable) {
                    if (runnable!=null) {
                        runnable.run();
                    }
                }
                }
            };
            timer.schedule(task, 5, 500);
        }
    }

    private static ArrayList<Runnable> timeRunnable = new ArrayList<>();

    public static void addTimeView(Runnable _timeRunnable) {
        if (timeRunnable==null)return;
        timeRunnable.add(_timeRunnable);
    }

    public static void removeTimeRunnable(Runnable runnable){
        timeRunnable.remove(runnable);
    }
}
