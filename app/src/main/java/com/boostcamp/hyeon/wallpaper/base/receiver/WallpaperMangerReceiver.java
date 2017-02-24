package com.boostcamp.hyeon.wallpaper.base.receiver;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.domain.Wallpaper;
import com.boostcamp.hyeon.wallpaper.base.util.AlarmManagerHelper;
import com.boostcamp.hyeon.wallpaper.base.util.Define;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import io.realm.Realm;

/**
 * Created by hyeon on 2017. 2. 20..
 */

public class WallpaperMangerReceiver extends BroadcastReceiver{
    private static String TAG = WallpaperMangerReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: "+intent.getAction());
        if(!SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_IS_USING_WALLPAPER, false))
            return;
        boolean isTransparentWallpaper = SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_IS_TRANSPARENT_WALLPAPER, false);
        long changeCycle = SharedPreferenceHelper.getInstance().getLong(SharedPreferenceHelper.Key.LONG_REPEAT_CYCLE_MILLS, 0);
        Log.d(TAG, "change cycle: "+changeCycle);

        if(!isTransparentWallpaper){
            Log.d(TAG, "default wallpaper");
            if (intent.getAction().equals(context.getString(R.string.wallpaper_set_action)) && changeCycle != Define.CHANGE_CYCLE_SCREEN_OFF) {
                setWallpaperManager(context, changeCycle);
            }else if(intent.getAction().equals(context.getString(R.string.wallpaper_set_action)) && changeCycle == Define.CHANGE_CYCLE_SCREEN_OFF){
                setWallpaperManager(context, changeCycle);
            }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF) && changeCycle == Define.CHANGE_CYCLE_SCREEN_OFF){
                setWallpaperManager(context, changeCycle);
            }
        }else{
            if(intent.getAction().equals(context.getString(R.string.wallpaper_set_action))){
                Log.d(TAG, "transparent wallpaper: start");
            }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                Log.d(TAG, "transparent wallpaper: screen off");
            }else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                Log.d(TAG, "transparent wallpaper: screen on");
            }
        }
    }

    private void setWallpaperManager(Context context, long changeCycle){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        Wallpaper wallpaper = realm.where(Wallpaper.class).findFirst();
        Image image = wallpaper.getImages().get(wallpaper.getNextPosition());
        int currentPosition = wallpaper.getNextPosition();
        int nextPosition;
        if(SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_IS_RANDOM_ORDER, false)){
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
        realm.commitTransaction();

        int changeScreenType = SharedPreferenceHelper.getInstance().getInt(SharedPreferenceHelper.Key.INT_CHANGE_SCREEN_TYPE, 0);
        setWallpaperFromChangeType(context, changeScreenType, Uri.parse(image.getImageUri()));

        if (wallpaper.getImages().size() != 1 && changeCycle != Define.CHANGE_CYCLE_SCREEN_OFF) {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(System.currentTimeMillis() + changeCycle);
            AlarmManagerHelper.registerToAlarmManager(context, date, Define.ID_ALARM_DEFAULT);
        }
    }

    private void setWallpaperFromChangeType(Context context, int changeScreenType, Uri imageUri){
        if(changeScreenType == Define.CHANGE_SCREEN_TYPE[Define.INDEX_TYPE_WALLPAPER]){
            setWallpaper(context, imageUri);
        }else if(changeScreenType == Define.CHANGE_SCREEN_TYPE[Define.INDEX_TYPE_LOCK_SCREEN]){
            setLockScreen(context, imageUri);
        }else{
            setLockScreen(context, imageUri);
            setWallpaper(context, imageUri);
        }
    }

    private void setWallpaper(Context context, Uri imageUri){
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver() , imageUri);
            wallpaperManager.setBitmap(bitmap);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setLockScreen(Context context, Uri imageUri){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver() , imageUri);
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
