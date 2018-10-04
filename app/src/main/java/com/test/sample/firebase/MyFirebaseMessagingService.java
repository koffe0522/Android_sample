package com.test.sample.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.test.sample.PushReceiveActivity;
import com.test.ramenapp.R;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // プッシュメッセージのdataに含めた値を取得
        Map<String, String> data = remoteMessage.getData();
        String contentId = data.get("id");
        String contentType = data.get("type");

        // Notificationを生成
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS);
        builder.setAutoCancel(true);

        // タップ時に呼ばれるIntentを生成
        Intent intent = new Intent(this, PushReceiveActivity.class);
        intent.putExtra(PushReceiveActivity.ARG_ID, contentId);
        intent.putExtra(PushReceiveActivity.ARG_TYPE, contentType);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Notification表示
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(Integer.parseInt(contentId), builder.build());

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, builder.build());
    }
}
