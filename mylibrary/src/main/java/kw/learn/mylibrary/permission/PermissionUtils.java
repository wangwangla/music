package kw.learn.mylibrary.permission;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @Auther jian xian si qi
 * @Date 2023/5/15 22:05
 */
public class PermissionUtils {
    public static void checkPermission(Activity context,String[] permissions,int requestcode){
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{permission}, requestcode);
            }
        }
    }
}
