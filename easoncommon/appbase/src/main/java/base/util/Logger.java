package base.util;

import android.util.Log;

import base.BaseApplication;

public class Logger {
    public static void d(String TAG, String msg) {
        if (BaseApplication.isDebug)
            Log.d(TAG, msg);
    }

    public static void w(String TAG, String msg) {
        Log.w(TAG, msg);
    }

    public static void e(String TAG, String msg) {
        Log.e(TAG, msg);
    }
}
