package com.bsuir.dontforget;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class AlarmReciever extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        Notification newMessageNotification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_date_range_24dp)
                .setContentTitle("Уведомление")//Title
                .setContentText(intent.getStringExtra("name"))//Text
                .addAction(null)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(intent.getIntExtra("id",0), newMessageNotification);
    }
}