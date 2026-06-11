package com.example.learnandroid.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

public class MusicControlWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        MusicControlWidgetUpdater.updateWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        MusicControlWidgetUpdater.updateAllWidgets(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context,
                                          AppWidgetManager appWidgetManager,
                                          int appWidgetId,
                                          Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        MusicControlWidgetUpdater.updateWidgets(context, appWidgetManager, new int[]{appWidgetId});
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        MusicControlWidgetUpdater.updateWidgets(
                context,
                AppWidgetManager.getInstance(context),
                AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, MusicControlWidgetProvider.class))
        );
    }
}

