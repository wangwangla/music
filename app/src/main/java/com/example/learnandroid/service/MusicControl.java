package com.example.learnandroid.service;

import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;

import com.example.learnandroid.constant.MusicManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicControl extends Binder {
   private MediaPlayer player;
   private Timer timer;

   public MusicControl(MediaPlayer player) {
      this.player = player;
   }

   public void setData(String path) {
      try {
         player.reset();
         player.setDataSource(path);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public void play() {
      try {
         player.prepare();
         player.start();//播放音乐
         player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
               MusicManager.playNext();
            }
         });
         addTimer();     //添加计时器
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void continueMusic() {
      try {
         player.start();//播放音乐
         player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
               MusicManager.playNext();
            }
         });
         addTimer();     //添加计时器
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void pausePlay() {
      player.pause();           //暂停播放音乐
   }

   public void continuePlay() {
      player.start();           //继续播放音乐
   }

   public void seekTo(int progress) {
      player.seekTo(progress);//设置音乐的播放位置
   }

   public void addTimer() {        //添加计时器用于设置音乐播放器中的播放进度条
      if (timer == null) {
         timer = new Timer();     //创建计时器对象
         TimerTask task = new TimerTask() {
            @Override
            public void run() {
               if (player == null) return;
               int duration = player.getDuration();                 //获取歌曲总时长
               int currentPosition = player.getCurrentPosition();//获取播放进度
//                    Message msg = C.handler.obtainMessage();//创建消息对象
               //将音乐的总时长和播放进度封装至消息对象中
               Bundle bundle = new Bundle();
               bundle.putInt("duration", duration);
               bundle.putInt("currentPosition", currentPosition);
//                    msg.setData(bundle);
               //将消息发送到主线程的消息队列
//                    MainActivity.handler.sendMessage(msg);
            }
         };
         timer.schedule(task, 5, 500);
      }
   }

   public boolean isPlaying() {
      return player.isPlaying();
   }
}