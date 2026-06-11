package com.example.learnandroid.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

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
}

