package com.boostcamp.hyeon.wallpaper.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.base.service.TransparentActivityCallService;
import com.boostcamp.hyeon.wallpaper.base.util.AlarmManagerHelper;

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
            // if device is restart, TransparentActivityCallService start
            Intent startServiceIntent = new Intent(context, TransparentActivityCallService.class);
            context.startService(startServiceIntent);

            //register wallpaper to AlarmManager
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(System.currentTimeMillis()+DELAY_MILLIS);
            AlarmManagerHelper.unregisterToAlarmManager(context);
            AlarmManagerHelper.registerToAlarmManager(context, date);
        }
    }
}
