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
import android.view.Display;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.domain.Wallpaper;
import com.boostcamp.hyeon.wallpaper.base.util.AlarmManagerHelper;
import com.boostcamp.hyeon.wallpaper.base.util.Define;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;

import java.io.IOException;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by hyeon on 2017. 2. 20..
 */

public class WallpaperMangerReceiver extends BroadcastReceiver {
    private static String TAG = WallpaperMangerReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: "+intent.getAction());
        Log.d(TAG, "onReceive: "+intent.getAction());

        long changeCycle = SharedPreferenceHelper.getInstance().getLong(SharedPreferenceHelper.Key.LONG_REPEAT_CYCLE_MILLS, 0);
        Log.d(TAG, "change cycle: "+changeCycle);
        if (intent.getAction().equals(context.getString(R.string.wallpaper_set_action)) && changeCycle != Define.CHANGE_CYCLE_SCREEN_OFF) {
            setWallpaperManager(context, changeCycle);
        }else if(intent.getAction().equals(context.getString(R.string.wallpaper_set_action)) && changeCycle == Define.CHANGE_CYCLE_SCREEN_OFF){
            setWallpaperManager(context, changeCycle);
        }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF) && changeCycle == Define.CHANGE_CYCLE_SCREEN_OFF){
            setWallpaperManager(context, changeCycle);
        }
    }

    private void setWallpaperManager(Context context, long changeCycle){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        Wallpaper wallpaper = realm.where(Wallpaper.class).findFirst();
        Image image = wallpaper.getImages().get(wallpaper.getCurrentPosition());
        int currentPosition = wallpaper.getCurrentPosition();
        if (currentPosition + 1 >= wallpaper.getImages().size()) {
            currentPosition = 0;
        } else {
            currentPosition++;
        }
        wallpaper.setCurrentPosition(currentPosition);
        realm.commitTransaction();

        String changeScreenType = SharedPreferenceHelper.getInstance().getString(SharedPreferenceHelper.Key.STRING_CHANGE_SCREEN_TYPE, null);
        setWallpaperFromChangeType(context, changeScreenType, Uri.parse(image.getImageUri()));

        if (wallpaper.getImages().size() != 1 && changeCycle != Define.CHANGE_CYCLE_SCREEN_OFF) {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(System.currentTimeMillis() + changeCycle);
            AlarmManagerHelper.registerToAlarmManager(context, date);
        }
    }

    private void setWallpaperFromChangeType(Context context, String changeScreenType, Uri imageUri){
        if(changeScreenType.equals(context.getString(R.string.label_wallpaper))){
            setWallpaper(context, imageUri);
        }else if(changeScreenType.equals(context.getString(R.string.label_lock_screen))){
            setLockScreen(context, imageUri);
        }else{
            setWallpaper(context, imageUri);
            setLockScreen(context, imageUri);
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
