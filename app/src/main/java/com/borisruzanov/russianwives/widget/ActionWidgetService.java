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

    String s;
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
            updateAppWidgetContent(getApplicationContext(), manager, appWidgetId);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateAppWidgetContent(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        //TODO create request to firebase for getting user with likes and visits
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_action);

        /*new FirebaseRepository().getActionsInfo((visits, likes) -> {
            Log.d("WidgetDebug", "Count: " + visits + " and " + likes);
            views.setTextViewText(R.id.widget_action_likes, visits);
            views.setTextViewText(R.id.widget_action_visits, likes);
        });*/

        views.addView(R.id.widget_container, views);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

}

