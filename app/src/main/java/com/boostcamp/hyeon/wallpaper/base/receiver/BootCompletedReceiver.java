package com.boostcamp.hyeon.wallpaper.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.base.service.TransparentActivityCallService;

/**
 * Created by hyeon on 2017. 2. 18..
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = BootCompletedReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Log.d(TAG , "ACTION_BOOT_COMPLETED" );
            // if device is restart, TransparentActivityCallService start
            Intent startServiceIntent = new Intent(context, TransparentActivityCallService.class);
            context.startService(startServiceIntent);

        }
    }
}
