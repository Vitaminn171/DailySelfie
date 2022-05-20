package com.example.dailyselfie;



import static androidx.core.content.ContextCompat.createDeviceProtectedStorageContext;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= 26)
        {
            String ID = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(ID, description, importance);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(context, ID)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.drawable.ic_camera)
                    .setContentTitle("Daily Selfie")
                    .setContentText("Time to take another selfie")
                    .setAutoCancel(true)
                    .build();
            manager.notify(1, notification);
        }
    }

}
