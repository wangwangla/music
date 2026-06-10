package kw.learn.mylibrary.edge;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public final class EdgeToEdgeCompat {
    // Matches the common Material/AndroidX edge-to-edge fallback scrims.
    private static final int LIGHT_SCRIM = 0xE6FFFFFF;
    private static final int DARK_SCRIM = 0x801B1B1B;

    private EdgeToEdgeCompat() {
    }

    public static void enable(Activity activity) {
        Window window = activity.getWindow();
        boolean lightSystemBarIcons = isLightTheme(activity);
        //系统不在系统栏周围添加任何内边距或调整内容大小以适应系统栏。应用程序负责处理系统栏和内容之间的任何重叠。
        WindowCompat.setDecorFitsSystemWindows(window, false);
        //处理底部和顶部颜色的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            applyApi29Plus(window, lightSystemBarIcons);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            applyApi26To28(window, lightSystemBarIcons);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            applyApi23To25(window, lightSystemBarIcons);
        } else {
            applyApi21To22(window);
        }

        View decorView = window.getDecorView();
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(window, decorView);
        if (controller != null) {
            controller.setAppearanceLightStatusBars(lightSystemBarIcons);
            controller.setAppearanceLightNavigationBars(
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && lightSystemBarIcons
            );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static void applyApi29Plus(Window window, boolean lightSystemBarIcons) {
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
        window.setStatusBarContrastEnforced(false);
        // Keep nav bar contrast enforcement for 3-button mode readability.
        window.setNavigationBarContrastEnforced(true);
    }

    private static void applyApi26To28(Window window, boolean lightSystemBarIcons) {
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(lightSystemBarIcons ? LIGHT_SCRIM : DARK_SCRIM);
    }

    private static void applyApi23To25(Window window, boolean lightSystemBarIcons) {
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(DARK_SCRIM);
    }

    private static void applyApi21To22(Window window) {
        window.setStatusBarColor(DARK_SCRIM);
        window.setNavigationBarColor(DARK_SCRIM);
    }

    private static boolean isLightTheme(Activity activity) {
        int nightMode = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode != Configuration.UI_MODE_NIGHT_YES;
    }
}
