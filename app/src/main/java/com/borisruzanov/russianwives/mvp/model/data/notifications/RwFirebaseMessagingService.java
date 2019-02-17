package com.borisruzanov.russianwives.mvp.model.data.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository;
import com.borisruzanov.russianwives.mvp.ui.chatmessage.ChatMessageActivity;
import com.borisruzanov.russianwives.utils.Consts;
import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.ExecutionException;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getDeviceToken;
import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class RwFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();
        String messageIcon = remoteMessage.getNotification().getIcon();
        String fromUid = remoteMessage.getData().get("fromUid");

        Log.d("NotificationDebug", "FromUid is " + fromUid);

        Intent resultIntent = new Intent(this, ChatMessageActivity.class);
        resultIntent.putExtra(Consts.UID, fromUid);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(this)
                    .asBitmap()
                    .load(messageIcon)
                    .submit().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        // TODO add user image into notification icon
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.default_avatar)
                .setAutoCancel(true)
                .setChannelId(getString(R.string.notification_channel_id))
                .setContentIntent(resultPendingIntent);

        if (bitmap != null) notificationBuilder.setLargeIcon(bitmap);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int notificationID = 507;
        notificationManager.notify(notificationID, notificationBuilder.build());

    }



    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("TokensDebug", "New token is " + s + " current token is " + getDeviceToken());
        if (getUid() != null) new UserRepository(new Prefs(getApplicationContext())).updateToken();
    }
}