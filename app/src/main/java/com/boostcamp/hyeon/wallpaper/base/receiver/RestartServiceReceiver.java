package com.boostcamp.hyeon.wallpaper.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.base.service.ImageFileObserverService;

/**
 * Created by hyeon on 2017. 2. 18..
 */

public class RestartServiceReceiver extends BroadcastReceiver {
    private static final String TAG = RestartServiceReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        // if ImageFileObserverService destroy, it restart
        if(intent.getAction().equals("ACTION.RESTART.ImageFileObserverService")){
            Log.d(TAG ,"ACTION.RESTART.ImageFileObserverService " );
            Intent restartServiceIntent = new Intent(context, ImageFileObserverService.class);
            context.startService(restartServiceIntent);
        }

        // if device is restart, ImageFileObserverService start
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Log.d(TAG , "ACTION_BOOT_COMPLETED" );
            Intent startServiceIntent = new Intent(context, ImageFileObserverService.class);
            context.startService(startServiceIntent);
        }
    }
}
