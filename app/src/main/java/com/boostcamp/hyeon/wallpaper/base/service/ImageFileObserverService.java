package com.boostcamp.hyeon.wallpaper.base.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Folder;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.receiver.RestartServiceReceiver;
import com.boostcamp.hyeon.wallpaper.base.util.ImageFileObserver;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by hyeon on 2017. 2. 17..
 */

public class ImageFileObserverService extends Service {
    private static final String TAG = ImageFileObserverService.class.getSimpleName();
    private ArrayList<ImageFileObserver> mObserverList = new ArrayList<>();
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

        initFileObserver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
        for(ImageFileObserver fileObserver : mObserverList){
            fileObserver.stopWatching();
        }
        registerToAlarmManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");
        return super.onStartCommand(intent, flags, startId);
    }

    private void initFileObserver(){
        Log.d(TAG, "initObserver called");
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        RealmResults<Folder> folderRealmResults = realm.where(Folder.class).findAll();
        Log.d(TAG, "folderRealmResults size: "+folderRealmResults.size());
        for(Folder folder : folderRealmResults) {
            Log.d(TAG, "image size: "+folder.getImages().size());
            for(Image image : folder.getImages()) {
                ImageFileObserver fileObserver = new ImageFileObserver(image.getImageUri(), getApplicationContext());
                fileObserver.startWatching();
                mObserverList.add(fileObserver);
            }
        }
        realm.commitTransaction();
    }

    private void registerToAlarmManager(){
        Log.d(TAG, "registerToAlarmManager");
        Intent intent = new Intent(ImageFileObserverService.this, RestartServiceReceiver.class);
        intent.setAction("ACTION.RESTART.ImageFileObserverService");
        PendingIntent sender = PendingIntent.getBroadcast(ImageFileObserverService.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 1000, sender);
    }

    private void unregisterToAlarmManager(){
        Log.d(TAG , "unregisterToAlarmManager" );
        Intent intent = new Intent(ImageFileObserverService.this, RestartServiceReceiver.class);
        intent.setAction("ACTION.RESTART.ImageFileObserverService");
        PendingIntent sender = PendingIntent.getBroadcast(ImageFileObserverService.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
