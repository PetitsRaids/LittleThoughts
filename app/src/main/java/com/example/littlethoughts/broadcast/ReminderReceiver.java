package com.example.littlethoughts.broadcast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.littlethoughts.MyApplication;
import com.example.littlethoughts.R;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderReceiver extends BroadcastReceiver {

    private static final String TAG = "ReminderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        Toast.makeText(context, "ACTION", Toast.LENGTH_SHORT).show();
        sendNotification();
    }

    private void sendNotification(){
        String channelId = "com.example.littlethoughts";
        String channelName = "Little Thoughts";
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MyApplication.getContext());
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000});
            notificationManagerCompat.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat
                .Builder(MyApplication.getContext(), channelId)
                .setContentTitle("Test")
                .setContentText("hahahahahaha")
                .setSmallIcon(R.mipmap.app_logo)
                .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getContext().getResources(),R.mipmap.app_logo))
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(1, notification);
    }

}
