package com.boostcamp.hyeon.wallpaper.base.receiver;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.domain.Wallpaper;
import com.boostcamp.hyeon.wallpaper.base.util.AlarmManagerHelper;
import com.boostcamp.hyeon.wallpaper.base.util.BitmapHelper;
import com.boostcamp.hyeon.wallpaper.base.util.Define;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import io.realm.Realm;

/**
 * Created by hyeon on 2017. 2. 20..
 */

public class WallpaperMangerReceiver extends BroadcastReceiver{
    private static final String TAG = WallpaperMangerReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: "+intent.getAction());
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        Wallpaper wallpaper = realm.where(Wallpaper.class).findFirst();
        if(wallpaper == null)
            wallpaper = realm.createObject(Wallpaper.class);
        if(wallpaper.isUsing()) {
            if (!wallpaper.isTransparent()) {
                if (intent.getAction().equals(context.getString(R.string.wallpaper_set_action))) {
                    Log.d(TAG, "default wallpaper");
                    setWallpaperManager(context, wallpaper);
                }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                    if(wallpaper.getChangeCycle() == Define.CHANGE_CYCLE_SCREEN_OFF)
                        setWallpaperManager(context, wallpaper);
                }
            } else {
                if (intent.getAction().equals(context.getString(R.string.wallpaper_set_action))) {
                    Log.d(TAG, "transparent wallpaper: start");
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    Log.d(TAG, "transparent wallpaper: screen off");
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    Log.d(TAG, "transparent wallpaper: screen on");
                }
            }
        }
        realm.commitTransaction();
    }

    private void setWallpaperManager(Context context, Wallpaper wallpaper){
        Log.d(TAG, "setWallpaperManager");
        Image image = wallpaper.getImages().get(wallpaper.getNextPosition());
        int currentPosition = wallpaper.getNextPosition();
        int nextPosition;
        if(wallpaper.isRandom()){
            int randomNumber;
            do {
                Random random = new Random();
                randomNumber = random.nextInt(wallpaper.getImages().size());
            }while (randomNumber == currentPosition);
            nextPosition = randomNumber;
        }else {
            if (currentPosition + 1 >= wallpaper.getImages().size()) {
                nextPosition = 0;
            } else {
                nextPosition = currentPosition + 1;
            }
        }
        wallpaper.setCurrentPosition(currentPosition);
        wallpaper.setNextPosition(nextPosition);

        setWallpaperFromChangeType(context, wallpaper.getChangeScreenType(), image.getImageUri(), Integer.valueOf(image.getOrientation()));

        // if not need repeat, example 1 image selected or change type is screen off.
        if (wallpaper.getImages().size() != 1 && wallpaper.getChangeCycle() != Define.CHANGE_CYCLE_SCREEN_OFF) {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(System.currentTimeMillis() + wallpaper.getChangeCycle());
            AlarmManagerHelper.registerToAlarmManager(context, date, Define.ID_ALARM_DEFAULT);
        }
    }

    private void setWallpaperFromChangeType(Context context, int changeScreenType, String imageUri, int orientation){
        Log.d(TAG, "setWallpaperFromChangeType: "+changeScreenType);

        int sampleSize;
        int width = BitmapHelper.getBitmapWidth(imageUri);
        int height = BitmapHelper.getBitmapHeight(imageUri);

        if(width <= Define.LIMIT_LOW_WIDTH_FOR_SAMPLE && height <= Define.LIMIT_LOW_HEIGHT_FOR_SAMPLE)
            sampleSize = Define.LOW_SAMPLE_SIZE;
        else if(width <= Define.LIMIT_MID_WIDTH_FOR_SAMPLE && height <= Define.LIMIT_MID_HEIGHT_FOR_SAMPLE)
            sampleSize = Define.MID_SAMPLE_SIZE;
        else
            sampleSize = Define.HIGH_SAMPLE_SIZE;

        Bitmap bitmap = BitmapHelper.decodeFile(imageUri, sampleSize);

        if(orientation != 0){
            bitmap = BitmapHelper.changeOrientation(bitmap, orientation);
        }

        if(changeScreenType == Define.CHANGE_SCREEN_TYPE[Define.INDEX_TYPE_WALLPAPER]){
            setWallpaper(context, bitmap);
        }else if(changeScreenType == Define.CHANGE_SCREEN_TYPE[Define.INDEX_TYPE_LOCK_SCREEN]){
            setLockScreen(context, bitmap);
        }else{
            setLockScreen(context, bitmap);
            setWallpaper(context, bitmap);
        }
    }

    private void setWallpaper(Context context, Bitmap bitmap){
        Log.d(TAG, "setWallpaper");
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            wallpaperManager.setBitmap(bitmap);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLockScreen(Context context, Bitmap bitmap){
        Log.d(TAG, "setLockScreen");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            try {
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
