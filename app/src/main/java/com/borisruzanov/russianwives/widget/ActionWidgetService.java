package com.borisruzanov.russianwives.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.ActionWidgetCallback;

public class ActionWidgetService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int [] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);

        for (int appWidgetId: widgetIds) {
            Log.d("WidgetDebug", "appwidgetId is " +  appWidgetId);
            updateAppWidgetContent(getApplicationContext(), manager, appWidgetId);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateAppWidgetContent(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_action);

        new FirebaseRepository().getActionsWidget((visits, likes) -> {
            views.setTextViewText(R.id.widget_action_likes, visits + " visits");
            views.setTextViewText(R.id.widget_action_visits, likes + " likes");
            Log.d("WidgetDebug", "User has " + visits + " and " + likes);
        });

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

}

