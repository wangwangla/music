package com.example.learnandroid.broad;

import android.app.Activity;
import android.content.IntentFilter;

import com.example.learnandroid.broadcast.MainBroadCast;
import com.example.learnandroid.constant.Constant;

/**
 * @Auther jian xian si qi
 * @Date 2023/5/24 7:39
 */
public class BroadUtils {
    public void setFilter(Activity activity) {
        MainBroadCast mainBroadCast = new MainBroadCast(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.UP_DATE_BOTTOM);
        activity.registerReceiver(mainBroadCast,filter);
    }
}
