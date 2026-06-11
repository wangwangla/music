package com.example.learnandroid.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;

import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.constant.MusicManager;
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

   public boolean setData(String path) {
      if (path == null || path.trim().isEmpty()) {
         return false;
      }
      try {
         player.reset();
         player.setDataSource(path);
         player.prepare();
         return true;
      } catch (Exception e) {
         try {
            player.reset();
         } catch (Exception ignored) {
         }
         return false;
      }
   }

   public long getCurrentPosition(){
      try {
         return player.getCurrentPosition();
      } catch (Exception ignored) {
         return 0L;
      }
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
      try {
         if (player.isPlaying()) {
            player.pause();           //暂停播放音乐
         }
      } catch (Exception ignored) {
      }
      mAudioManager.abandonAudioFocus(null); // 指定焦点变化的回调，可以为null
   }

   public void continuePlay() {
      int result = mAudioManager.requestAudioFocus(
              focusChangeListener,
              AudioManager.STREAM_MUSIC,
              AudioManager.AUDIOFOCUS_GAIN
      );
      if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
         try {
            player.start();           //继续播放音乐
         } catch (Exception ignored) {
         }
      } else {
         mAudioManager.abandonAudioFocus(null); // 指定焦点变化的回调，可以为null
      }
   }

   public void seekTo(int progress) {
      try {
         player.seekTo(progress);//设置音乐的播放位置
      } catch (Exception ignored) {
      }
   }


   public boolean isPlaying() {
      try {
         return player.isPlaying();
      } catch (Exception ignored) {
         return false;
      }
   }

   public int getPosition(){
      try {
         return player.getCurrentPosition();
      } catch (Exception ignored) {
         return 0;
      }
   }

   public void stop() {
      try {
         player.stop();
      } catch (Exception ignored) {
      }
      backAudioFocus();
   }

   public void backAudioFocus(){
      mAudioManager.abandonAudioFocus(null); // 指定焦点变化的回调，可以为null
   }
}