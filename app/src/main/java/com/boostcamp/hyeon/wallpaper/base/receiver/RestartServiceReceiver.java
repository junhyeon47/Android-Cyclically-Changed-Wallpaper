package com.boostcamp.hyeon.wallpaper.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.service.WallpaperManagerService;

/**
 * Created by hyeon on 2017. 2. 18..
 */

public class RestartServiceReceiver extends BroadcastReceiver {
    private static final String TAG = RestartServiceReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        // if WallpaperManagerService destroy, it restart
        if(intent.getAction().equals(context.getString(R.string.restart_service_action))){
            Log.d(TAG ,"ACTION.RESTART.WallpaperManagerService " );
            Intent restartServiceIntent = new Intent(context, WallpaperManagerService.class);
            context.startService(restartServiceIntent);
        }
    }
}
