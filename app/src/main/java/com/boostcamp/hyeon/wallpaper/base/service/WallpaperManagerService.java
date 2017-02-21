package com.boostcamp.hyeon.wallpaper.base.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.receiver.RestartServiceReceiver;
import com.boostcamp.hyeon.wallpaper.base.receiver.WallpaperMangerReceiver;
import com.boostcamp.hyeon.wallpaper.base.util.TransparentActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hyeon on 2017. 2. 17..
 */

public class WallpaperManagerService extends Service {
    private static final String TAG = WallpaperManagerService.class.getSimpleName();
    private static final int REPEAT_CYCLE = 3*60*1000;
    private WallpaperMangerReceiver mWallpaperMangerReceiver;
    private TimerTask mTask;
    private Timer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called");

        unregisterToAlarmManager();

        ForeGroundService.startForeground(this);
        Intent localIntent = new Intent(this, ForeGroundService.class);
        startService(localIntent);
        initWallpaperMangerReceiver();
        initTimerForTransparentActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
        unregisterReceiver(mWallpaperMangerReceiver);
        registerToAlarmManager();
        mTimer.cancel();
        mTask.cancel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");
        return super.onStartCommand(intent, flags, startId);
    }

    private void registerToAlarmManager(){
        Log.d(TAG, "registerToAlarmManager");
        Intent intent = new Intent(WallpaperManagerService.this, RestartServiceReceiver.class);
        intent.setAction(getString(R.string.restart_service_action));
        PendingIntent sender = PendingIntent.getBroadcast(WallpaperManagerService.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 1000, sender);
    }

    private void unregisterToAlarmManager(){
        Log.d(TAG , "unregisterToAlarmManager" );
        Intent intent = new Intent(WallpaperManagerService.this, RestartServiceReceiver.class);
        intent.setAction(getString(R.string.restart_service_action));
        PendingIntent sender = PendingIntent.getBroadcast(WallpaperManagerService.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private void initWallpaperMangerReceiver(){
        mWallpaperMangerReceiver = new WallpaperMangerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mWallpaperMangerReceiver, intentFilter);
    }

    private void initTimerForTransparentActivity(){
        mTask = new TimerTask() {
            @Override
            public void run() {
                PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
                boolean isScreenOn;
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH){
                    isScreenOn = powerManager.isScreenOn();
                }else{
                    isScreenOn = powerManager.isInteractive();
                }
                if(!isScreenOn) {
                    Intent intent = new Intent(WallpaperManagerService.this, TransparentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 0, REPEAT_CYCLE);
    }
}
