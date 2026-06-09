package kw.learn.mylibrary.permission;

import android.app.Activity;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @Auther jian xian si qi
 * @Date 2023/5/15 22:05
 */
public class PermissionUtils {
    public static boolean checkPermission(Activity context,String[] permissions,int requestcode){
        if (permissions == null || permissions.length == 0) {
            return true;
        }

        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                needRequest.add(permission);
            }
        }

        if (needRequest.isEmpty()) {
            return true;
        }

        ActivityCompat.requestPermissions(context,
                needRequest.toArray(new String[0]),
                requestcode);
        return false;
    }
}
