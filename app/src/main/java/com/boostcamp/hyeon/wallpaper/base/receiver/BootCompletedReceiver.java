package com.boostcamp.hyeon.wallpaper.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.base.service.WallpaperManagerService;
import com.boostcamp.hyeon.wallpaper.base.util.AlarmManagerHelper;
import com.boostcamp.hyeon.wallpaper.base.util.Define;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;

import java.util.Calendar;

/**
 * Created by hyeon on 2017. 2. 18..
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = BootCompletedReceiver.class.getSimpleName();
    private static final int DELAY_MILLIS = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Log.d(TAG , "ACTION_BOOT_COMPLETED" );
            // if device is restart, WallpaperManagerService start
            Intent startServiceIntent = new Intent(context, WallpaperManagerService.class);
            context.startService(startServiceIntent);

            int alarmId;
            if(SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_IS_TRANSPARENT_WALLPAPER, false)){
                alarmId = Define.ID_ALARM_TRANSPARENT;
            }else{
                alarmId = Define.ID_ALARM_DEFAULT;
            }
            //register wallpaper to AlarmManager
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(System.currentTimeMillis()+DELAY_MILLIS);
            AlarmManagerHelper.unregisterToAlarmManager(context, alarmId);
            AlarmManagerHelper.registerToAlarmManager(context, date, alarmId);
        }
    }
}
