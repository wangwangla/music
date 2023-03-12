package com.example.learnandroid.notification.lesslevel;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TargetApi(9)
public class NotificationCompatBase {
    private static Method sSetLatestEventInfo;
    public static Notification add(Notification notification, Context context,
                                   CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
        if (sSetLatestEventInfo == null) {
            try {
                sSetLatestEventInfo = Notification.class.getMethod("setLatestEventInfo",
                        Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            sSetLatestEventInfo.invoke(notification, context,
                    contentTitle, contentText, contentIntent);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return notification;
    }
}
