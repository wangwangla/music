package com.example.learnandroid.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;

import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.constant.MusicManager;

import java.io.IOException;

public class MusicControl extends Binder {
   private MediaPlayer player;
   private AudioManager mAudioManager;

   private AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
      @Override
      public void onAudioFocusChange(int focusChange) {
         System.out.println("-------------------------------------");
//         if (D) Log.d(TAG, "Received audio focus change event " + msg.arg1);
         switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
               if (MusicManager.isPlaying()) {
                  MusicManager.lossAudioFocus(1);
//                  service.mPausedByTransientLossOfFocus =
//                          msg.arg1 == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
               }
               MusicManager.pausePlay();
               break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//               removeMessages(FADEUP);
//               sendEmptyMessage(FADEDOWN);
               break;
            case AudioManager.AUDIOFOCUS_GAIN:
               if (!MusicManager.isPlaying()
                       && MusicManager.getLossFocuStatus()==1) {
                  MusicManager.lossAudioFocus(0);
                  MusicManager.play();
               }
               break;
         }
      }
   };

   public MusicControl(MediaPlayer player) {
      this.player = player;
      mAudioManager = (AudioManager) MusicApplication.getMusicContent().getSystemService(Context.AUDIO_SERVICE);

   }

   public void setData(String path) {
      try {
         player.reset();
         player.setDataSource(path);
         player.prepare();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public long getCurrentPosition(){
      return player.getCurrentPosition();
   }

   public void play() {
      try {

         continuePlay();
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
      mAudioManager.abandonAudioFocus(null); // 指定焦点变化的回调，可以为null
   }

   public void continuePlay() {
      //获取焦点
      int result = mAudioManager.requestAudioFocus(
              focusChangeListener, // 指定焦点变化的回调，可以为null
              AudioManager.STREAM_MUSIC, // 指定音频流的类型
              AudioManager.AUDIOFOCUS_GAIN // 指定请求焦点类型
      );
      if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
         // 成功获得焦点，可以播放音频
//         mAudioManager.requestAudioFocus(, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
         player.start();           //继续播放音乐
      } else {
         // 未获得焦点，需要停止播放音频
         mAudioManager.abandonAudioFocus(null); // 指定焦点变化的回调，可以为null
      }
   }

   public void seekTo(int progress) {
      player.seekTo(progress);//设置音乐的播放位置
   }


   public boolean isPlaying() {
      return player.isPlaying();
   }

   public int getPosition(){
      return player.getCurrentPosition();
   }

   public void stop() {
      player.stop();
      backAudioFocus();
   }

   public void backAudioFocus(){
      mAudioManager.abandonAudioFocus(null); // 指定焦点变化的回调，可以为null
   }
}