package com.example.littlethoughts.utils;

import android.util.Log;

import com.example.littlethoughts.BuildConfig;

public class Logger {

    private static final String TAG = "Petits_Raids";

    private static final boolean isDebugging = BuildConfig.DEBUG;

    public static void d(String msg) {
        if (!isDebugging)
            return;
        Log.d(TAG, msg);
    }

    public static void d(int msg) {
        if (!isDebugging)
            return;
        Log.d(TAG, "" + msg);
    }

}
