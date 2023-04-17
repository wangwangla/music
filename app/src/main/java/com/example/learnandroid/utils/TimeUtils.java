package com.example.learnandroid.utils;

public class TimeUtils {
    private static StringBuilder builder = new StringBuilder();
    public static String longToTime(long time){
        builder.setLength(0);
        long m = time / 1000;
        long f = m / 60;
        long mm = m % 60;
        if (f<10){
            builder.append(0);
            builder.append(f);
        }else {
            builder.append(f);
        }
        builder.append(":");
        if (mm<10){
            builder.append(0);
            builder.append(mm);
        }else {
            builder.append(mm);
        }
        return builder.toString();
    }

    public static int miao(long time){
        long l = time / 1000;
        return (int) l;
    }

}
