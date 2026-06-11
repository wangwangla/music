package com.example.learnandroid.constant;

import com.example.learnandroid.application.utils.PlaybackStateStore;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.service.MusicControl;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MusicManager {
    private static long id = -1;
    private static int position = 0;
    public static MusicControl musicController;
    private static ArrayList<MusicBean> musicBeans;
    private static boolean isPause = false;
    private static ArrayList<Runnable> musicListener = new ArrayList<>();
    private static ArrayList<Runnable> timeRunnable = new ArrayList<>();

    public static void addUpdateView(Runnable runnable){
        musicListener.add(runnable);
    }

    public static void removeRunnable(Runnable runnable){
        musicListener.remove(runnable);
    }

    public static boolean hasSongList() {
        return musicBeans != null && !musicBeans.isEmpty();
    }

    public static void setData(){
        setData(position);
    }

    public static void updateListener(){
        for (Runnable runnable : musicListener) {
            runnable.run();
        }
    }

    /**
     * point music id
     * @param index
     * @return
     */
    public static boolean setData(long index) {
        if (!hasSongList()) return false;
        int tempIndex = -1;
        for (MusicBean musicBean : musicBeans) {
            tempIndex++;
            if (musicBean.getId() == index) {
                setData(tempIndex);
                return true;
            }
        }
        return false;
    }

    public static boolean setData(int index) {
       return setData(index,false);
    }

    public static boolean setData(int index,boolean isListener){
        if (!hasSongList())return false;
        if (index < 0 || index>=musicBeans.size())return false;
        if (musicController == null) return false;
        MusicBean musicBean = musicBeans.get(index);
        if (id == musicBean.getId())return true;
        isPause = false;
        position = index;
        setCurrentPlayId(musicBean.getId());
        musicController.setData(musicBean.getPath());
        persistStateSnapshot();
        if (isListener) {
            updateListener();
        }
        return true;
    }

    public static long getCurrentPosition(){
        if (musicController == null) return 0L;
        return musicController.getCurrentPosition();
    }

    public static void setDataAndplay() {
        if (musicController == null) return;
        addTimer();
        musicController.play();
        isPause = false;
        persistStateSnapshot();
        updateListener();
    }

    public static void pausePlay() {
        if (musicController == null) {
            isPause = true;
            persistStateSnapshot();
            return;
        }
        isPause = true;
        musicController.pausePlay();           //暂停播放音乐
        persistStateSnapshot();
        updateListener();
    }

    public static void continuePlay() {
        if (musicController == null) return;
        if (isPause){
            isPause = false;
            musicController.continuePlay();           //继续播放音乐
            persistStateSnapshot();
            updateListener();
            addTimer();
        }else {
            setData();
            setDataAndplay();
        }
    }

    public static void seekTo(int progress) {
        if (musicController == null) return;
        musicController.seekTo(progress);//设置音乐的播放位置
        persistStateSnapshot();
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
        musicBeans = _musicBeans == null ? new ArrayList<MusicBean>() : new ArrayList<>(_musicBeans);
        if (!checkposition(position)) {
            position = 0;
        }
        persistStateSnapshot();
    }

    public static void playNext() {
        if (!hasSongList()) return;
        int index = 0;
        if (Constant.playStyle == 0) {
            position++;
            if (musicBeans.size() <= position) {
                position = 0;
            }
            index = position;
        } else {
            int size = musicBeans.size();
            int previousPosition = position;
            if (size == 1) {
                position = 0;
            } else {
                do {
                    position = (int) (Math.random() * size);
                } while (position == previousPosition);
            }
            index = position;
        }
        setDataAndplay(index);
    }


    public static void playPre() {
        if (!hasSongList()) return;
        int index = 0;
        if (Constant.playStyle == 0) {
            position--;
            if (position<0) {
                position = musicBeans.size()-1;
            }
            index = position;
        } else {
            ArrayList<MusicBean> copy = new ArrayList<>();
            copy.addAll(musicBeans);
            if (copy.size()>1) {
                copy.remove(musicBeans.get(position));
                int v = (int)(Math.random() * copy.size());
                MusicBean musicBean = copy.get(v);
                for (int i = 0; i < musicBeans.size(); i++) {
                    if (musicBeans.get(i).getId() == musicBean.getId()) {
                        index = i;
                    }
                }
            }else {
                index = 0;
            }
        }
        setDataAndplay(index);
    }

    public static MusicBean getMusicBean() {
        if (!checkposition(position))return null;
        return musicBeans.get(position);
    }

    public static boolean checkposition(int position){
        if (musicBeans == null) return false;
        if (position < 0 )return false;
        if (position >= musicBeans.size())return false;
        return true;
    }

    public static boolean isPlaying(){
        if (musicController == null) return false;
        return musicController.isPlaying();
    }

    public static long getDuration() {
        if (!checkposition(position))return 0L;
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
                //因为两个线程，所以偶尔会崩掉
                try {
                    for (Runnable runnable : timeRunnable) {
                        if (runnable!=null) {
                            runnable.run();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                }
            };
            timer.schedule(task, 5, 100);
        }
    }

    public static void addTimeView(Runnable _timeRunnable) {
        if (timeRunnable==null)return;
        if (timeRunnable.contains(_timeRunnable))return;
        timeRunnable.add(_timeRunnable);
    }

    public static void removeTimeRunnable(Runnable runnable){
        timeRunnable.remove(runnable);
    }

    public static void stop() {
        if (musicController == null) return;
        musicController.stop();
        isPause = true;
        persistStateSnapshot();
    }

    public static void setDataAndplay(long id) {
        if (setData(id)) {
            play();
        }
    }

    //
    public static void setDataAndplay(int id) {
        if (setData(id)) {
            play();
        }
    }

    public static void play(){
        if (musicController == null) return;
        musicController.continuePlay();
        isPause = false;
        addTimer();
        persistStateSnapshot();
        updateListener();
    }

    private static int audioFocusStatus;
    public static void lossAudioFocus(int _audioFocusStatus) {
        audioFocusStatus = _audioFocusStatus;
    }

    public static int getLossFocuStatus(){
        return audioFocusStatus;
    }

    public static void persistStateSnapshot() {
        MusicBean currentSong = getMusicBean();
        String currentPath = currentSong != null ? currentSong.getPath() : null;
        int seekPosition = 0;
        try {
            seekPosition = (int) getCurrentPosition();
        } catch (Exception ignored) {
        }
        boolean wasPlaying = musicController != null ? musicController.isPlaying() : (!isPause && currentSong != null);
        PlaybackStateStore.saveSnapshot(
                musicBeans,
                position,
                currentSong != null ? currentSong.getId() : id,
                currentPath,
                wasPlaying,
                seekPosition,
                Constant.playStyle
        );
    }
}
