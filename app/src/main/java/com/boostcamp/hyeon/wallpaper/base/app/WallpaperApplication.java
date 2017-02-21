package com.boostcamp.hyeon.wallpaper.base.app;

import android.app.Application;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.boostcamp.hyeon.wallpaper.base.util.MediaScannerConnectionHelper;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public class WallpaperApplication extends Application{
    //device screen size variable
    public int mDeviceWidthSize;
    public int mDeviceHeightSize;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());

        //init device screen size
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mDeviceWidthSize = displayMetrics.widthPixels;
        mDeviceHeightSize = displayMetrics.heightPixels;

        //init SharedPreferences
        SharedPreferenceHelper.getInstance(getApplicationContext());

        //init MediaScannerConnection
        MediaScannerConnectionHelper.getInstance(getApplicationContext());
    }

    public static Realm getRealmInstance(){
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        return Realm.getInstance(config);
    }

}
