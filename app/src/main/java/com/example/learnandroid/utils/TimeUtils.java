package com.example.learnandroid.utils;

public class TimeUtils {
    public static String longToTime(long time){
        long m = time / 1000;
        long f = m / 60;
        long mm = m % 60;
        return f+":"+mm;
    }
}
