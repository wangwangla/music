package com.example.learnandroid.broad;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Build;

import com.example.learnandroid.broadcast.MainBroadCast;

/**
 * @Auther jian xian si qi
 * @Date 2023/5/24 7:39
 */
public class BroadUtils {
    public void setFilter(Activity activity,String str[]) {
        MainBroadCast mainBroadCast = new MainBroadCast(activity);
        IntentFilter filter = new IntentFilter();
        for (String s : str) {
//            filter.addAction(Constant.UP_DATE_BOTTOM);
            filter.addAction(s);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.registerReceiver(mainBroadCast, filter, Activity.RECEIVER_NOT_EXPORTED);
        } else {
            activity.registerReceiver(mainBroadCast, filter);
        }
    }
}
