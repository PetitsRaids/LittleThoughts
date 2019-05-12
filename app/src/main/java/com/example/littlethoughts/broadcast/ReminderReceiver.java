package com.example.littlethoughts.broadcast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.example.littlethoughts.MyApplication;
import com.example.littlethoughts.R;
import com.example.littlethoughts.service.ReminderService;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.littlethoughts.REMINDER")) {
            sendNotification(intent.getIntExtra("test", -1));
        } else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            ReminderService.enqueueWork(context, new Intent());
        }
    }

    private void sendNotification(int setTime) {
        String channelId = "com.example.littlethoughts";
        String channelName = "Little Thoughts";
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MyApplication.getContext());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000});
            notificationManagerCompat.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat
                .Builder(MyApplication.getContext(), channelId)
                .setContentTitle("Test")
                .setContentText(String.valueOf(setTime))
                .setSmallIcon(R.mipmap.app_logo)
                .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.mipmap.app_logo))
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(1, notification);
    }

}
