package com.example.littlethoughts.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class ReminderService extends JobIntentService {

    private static final String TAG = "ReminderService";

    private AlarmManager alarmManager;

    private PendingIntent pendingIntent;

    public ReminderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        new Thread(() -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 21, 0, 0);
            Intent setAlarm = new Intent("com.example.littlethoughts.REMINDER");
            setAlarm.setPackage("com.example.littlethoughts");
            pendingIntent = PendingIntent.getBroadcast(this, 2, setAlarm, 0);
            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
            stopSelf();
        }).start();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }

    public static void enqueueWork(Context context , Intent intent){
        enqueueWork(context, ReminderService.class, 1, intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private void cancelAlarm() {
        alarmManager.cancel(pendingIntent);
    }

}
