package com.example.learnandroid.notification.lesslevel;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;
/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 22:48
 */
public class NotificationCompat {

    static final NotificationCompatImplBase IMPL;

    static class NotificationCompatImplBase {
        public Notification build(Builder b) {
            Notification result = b.mNotification;
            result = NotificationCompatBase.add(result, b.mContext,
                    b.mContentTitle, b.mContentText, b.mContentIntent);
            return result;
        }

    }

    static {
        IMPL = new NotificationCompatImplBase();
    }

    public static class Builder {
        private static final int MAX_CHARSEQUENCE_LENGTH = 5 * 1024;
        public Context mContext;
        public CharSequence mContentTitle;
        public CharSequence mContentText;
        PendingIntent mContentIntent;
        public Notification mNotification = new Notification();

        public Builder(Context context) {
            mContext = context;
            mNotification.when = System.currentTimeMillis();
            mNotification.audioStreamType = Notification.STREAM_DEFAULT;
        }

        public Builder setSmallIcon(int icon) {
            mNotification.icon = icon;
            return this;
        }

        public Builder setContentTitle(CharSequence title) {
            mContentTitle = limitCharSequenceLength(title);
            return this;
        }

        public Builder setContentText(CharSequence text) {
            mContentText = limitCharSequenceLength(text);
            return this;
        }

        public Builder setContent(RemoteViews views) {
            mNotification.contentView = views;
            return this;
        }

        public Builder setContentIntent(PendingIntent intent) {
            mContentIntent = intent;
            return this;
        }

        public Notification build() {
            return IMPL.build(this);
        }

        protected static CharSequence limitCharSequenceLength(CharSequence cs) {
            if (cs == null) return cs;
            if (cs.length() > MAX_CHARSEQUENCE_LENGTH) {
                cs = cs.subSequence(0, MAX_CHARSEQUENCE_LENGTH);
            }
            return cs;
        }
    }
}
