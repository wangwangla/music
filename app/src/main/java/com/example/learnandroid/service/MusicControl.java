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

   public long getCurrentPosition(){
      return player.getCurrentPosition();
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


   public boolean isPlaying() {
      return player.isPlaying();
   }
}