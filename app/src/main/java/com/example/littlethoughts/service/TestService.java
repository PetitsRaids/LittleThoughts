package com.example.littlethoughts.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.littlethoughts.R;

import java.util.Calendar;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TestService extends Service {

    private static final String TAG = "TestService";

    MyBinder myBinder = new MyBinder();

    public TestService() {
    }

    public class MyBinder extends Binder {
        public void showNotificationAfter(int second){
            Calendar calendar = Calendar.getInstance();
            long atTime = calendar.getTimeInMillis() + (second * 1000);
            showNotification(atTime);
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private void showNotification(long afterTime){
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        String channelId = "com.example.littlethoughts";
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            String channelName = "TestService";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManagerCompat.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setAutoCancel(true)
                .setContentTitle("TestService Notification")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.black_point)
                .setWhen(afterTime)
                .setShowWhen(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.add_white))
                .build();
        notificationManagerCompat.notify(1, notification);
    }
}
