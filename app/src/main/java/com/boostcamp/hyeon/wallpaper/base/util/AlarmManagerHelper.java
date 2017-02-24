package com.boostcamp.hyeon.wallpaper.base.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.receiver.WallpaperMangerReceiver;

import java.util.Calendar;

/**
 * Created by hyeon on 2017. 2. 20..
 */

public class AlarmManagerHelper {

    public static void registerToAlarmManager(Context context, Calendar date, int alarmId){
        Intent intent = new Intent(context, WallpaperMangerReceiver.class);
        intent.setAction(context.getString(R.string.wallpaper_set_action));
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long now = System.currentTimeMillis();
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTimeInMillis(now);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setAlarmClock(new AlarmManager.AlarmClockInfo(date.getTimeInMillis(), sender), sender);
        } else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                manager.setExact(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), sender);
            } else {
                manager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), sender);
            }
        }
    }

    public static void unregisterToAlarmManager(Context context, int alarmId){
        Intent intent = new Intent(context, WallpaperMangerReceiver.class);
        intent.setAction(context.getString(R.string.wallpaper_set_action));
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(sender);
    }
}
