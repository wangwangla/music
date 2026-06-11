package com.example.learnandroid.session;

import android.content.Context;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;


import com.example.learnandroid.constant.MusicManager;

/**
 * @Auther jian xian si qi
 * @Date 2023/5/24 7:32
 */
public class SessionUtils {
    private MediaSessionCompat mSession;
    private MediaSessionCompat.Callback mediasessionBack;

    public SessionUtils(Context context){
        mSession = new MediaSessionCompat(context, "Music");
        mSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
                | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        mediasessionBack = getMediaSessionCallback();
        mSession.setCallback(mediasessionBack);
    }

    private MediaSessionCompat.Callback getMediaSessionCallback() {
        return new MediaSessionCompat.Callback() {

            @Override
            public void onPause() {
                MusicManager.pausePlay();
            }

            @Override
            public void onPlay() {
                if (MusicManager.isPlaying()) {
                    MusicManager.pausePlay();
                } else {
                    MusicManager.continuePlay();
                }
            }

            @Override
            public void onSeekTo(long pos) {
                MusicManager.seekTo((int) pos);
            }

            @Override
            public void onSkipToNext() {
                MusicManager.playNext();
            }

            @Override
            public void onSkipToPrevious() {
                MusicManager.playPre();
            }

            @Override
            public void onStop() {
                MusicManager.stop();
            }
        };
    }

    public void setMetadata(MediaMetadataCompat metadata) {
        mSession.setMetadata(metadata);
    }

    public MediaSessionCompat.Token getSessionToken() {
        return mSession.getSessionToken();
    }

    public MediaSessionCompat getmSession() {
        return mSession;
    }
}
