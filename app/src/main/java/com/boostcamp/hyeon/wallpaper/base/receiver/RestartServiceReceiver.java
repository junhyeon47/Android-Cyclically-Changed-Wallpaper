package com.boostcamp.hyeon.wallpaper.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.base.service.TransparentActivityCallService;

/**
 * Created by hyeon on 2017. 2. 18..
 */

public class RestartServiceReceiver extends BroadcastReceiver {
    private static final String TAG = RestartServiceReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        // if TransparentActivityCallService destroy, it restart
        if(intent.getAction().equals("ACTION.RESTART.TransparentActivityCallService")){
            Log.d(TAG ,"ACTION.RESTART.TransparentActivityCallService " );
            Intent restartServiceIntent = new Intent(context, TransparentActivityCallService.class);
            context.startService(restartServiceIntent);
        }
    }
}
