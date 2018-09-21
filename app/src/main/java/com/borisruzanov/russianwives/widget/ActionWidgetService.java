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
import com.borisruzanov.russianwives.mvp.model.interactor.WidgetInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;

public class ActionWidgetService extends Service {

    private WidgetInteractor interactor;

    public ActionWidgetService() {
        interactor = new WidgetInteractor(new FirebaseRepository());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int [] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);

        if(widgetIds != null) {
            for (int appWidgetId : widgetIds) {
                Log.d("WidgetDebug", "appwidgetId is " + appWidgetId);
                interactor.getActionsInfo((visits, likes) ->
                        updateAppWidgetContent(getApplicationContext(), manager, appWidgetId,
                        visits + " " + getApplicationContext().getString(R.string.visits),
                                likes + " " + getApplicationContext().getString(R.string.likes)));

            }
        }
        else Log.d("WidgetDebug",  "Widgets are null");

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateAppWidgetContent(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
                                        String visits, String likes) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_action);

            views.setTextViewText(R.id.widget_action_likes, likes);
            views.setTextViewText(R.id.widget_action_visits, visits);
            Log.d("WidgetDebug", "User has " + visits + " and " + likes);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

}

