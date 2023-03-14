package com.example.learnandroid.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/13 22:50
 */
public class App {
    public static final String CMDNAME = "command";
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String command = intent.getStringExtra(CMDNAME);
            handleCommandIntent(intent);
        }
    };


    public static final String SERVICECMD = "com.naman14.timber.musicservicecommand";
    private void handleCommandIntent(Intent intent) {
        final String action = intent.getAction();
    }

}