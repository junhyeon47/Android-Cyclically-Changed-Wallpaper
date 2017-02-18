package com.boostcamp.hyeon.wallpaper.base.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.R;

/**
 * Created by hyeon on 2017. 2. 18..
 */

public class ForeGroundService extends Service {
    private static final String TAG = ForeGroundService.class.getSimpleName();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        startForeground(this);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        stopForeground(true);
    }
    public static void startForeground(Service service){
        if(service != null){
            Notification notification = getNotification(service);
            if(notification != null){
                service.startForeground(1220, notification);
                Log.d(TAG, "startForeground");
            }
        }
    }
    public static Notification getNotification(Context paramContext){
        int smallIcon = R.mipmap.ic_launcher;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            smallIcon = R.mipmap.ic_launcher;
        }
        Notification notification =  new NotificationCompat.Builder(paramContext)
                .setSmallIcon(smallIcon)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setAutoCancel(true)
                .setWhen(0)
                .setTicker("").build();
        notification.flags = 16;
        return  notification;
    }
}