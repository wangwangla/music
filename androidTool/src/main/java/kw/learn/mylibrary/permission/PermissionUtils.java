package kw.learn.mylibrary.permission;

import android.app.Activity;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    /**
     * 新版权限回调，基于 Activity Result API。
     */
    public interface PermissionResultCallback {
        void onResult(boolean allGranted, Map<String, Boolean> result);
    }

    /**
     * 在 Activity / Fragment 的成员变量中提前注册。
     *
     * 用法：
     * 1. 先在 onCreate 前后的生命周期早期注册 launcher。
     * 2. 再调用 checkPermission(context, permissions, launcher, callback)。
     */
    public static ActivityResultLauncher<String[]> registerPermissionLauncher(
            ActivityResultCaller caller,
            final PermissionResultCallback callback) {
        return caller.registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    if (callback != null) {
                        callback.onResult(isAllGranted(result), new LinkedHashMap<>(result));
                    }
                });
    }

    /**
     * 新版权限请求方式：
     * 已授权时直接回调；未授权时通过 launcher 发起请求。
     *
     * @return true 表示当前已经全部授权；false 表示已发起权限申请，等待异步回调。
     */
    public static boolean checkPermission(
            Activity context,
            String[] permissions,
            ActivityResultLauncher<String[]> launcher,
            PermissionResultCallback callback) {
        Map<String, Boolean> currentResult = buildPermissionResult(context, permissions);
        if (currentResult.isEmpty() || isAllGranted(currentResult)) {
            if (callback != null) {
                callback.onResult(true, currentResult);
            }
            return true;
        }

        if (launcher == null) {
            throw new IllegalArgumentException("launcher can not be null when requesting permissions.");
        }

        launcher.launch(getDeniedPermissions(currentResult));
        return false;
    }

    public static String[] getDeniedPermissions(Activity context, String[] permissions) {
        return getDeniedPermissions(buildPermissionResult(context, permissions));
    }

    private static Map<String, Boolean> buildPermissionResult(Activity context, String[] permissions) {
        Map<String, Boolean> result = new LinkedHashMap<>();
        if (permissions == null || permissions.length == 0) {
            return result;
        }

        for (String permission : permissions) {
            boolean granted = ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED;
            result.put(permission, granted);
        }
        return result;
    }

    private static String[] getDeniedPermissions(Map<String, Boolean> result) {
        List<String> deniedPermissions = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : result.entrySet()) {
            if (!Boolean.TRUE.equals(entry.getValue())) {
                deniedPermissions.add(entry.getKey());
            }
        }
        return deniedPermissions.toArray(new String[0]);
    }

    private static boolean isAllGranted(Map<String, Boolean> result) {
        for (Boolean granted : result.values()) {
            if (!Boolean.TRUE.equals(granted)) {
                return false;
            }
        }
        return true;
    }
}
