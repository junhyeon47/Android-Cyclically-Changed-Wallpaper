package com.boostcamp.hyeon.wallpaper.base.util;

import com.boostcamp.hyeon.wallpaper.R;

/**
 * Created by hyeon on 2017. 2. 21..
 */

public class Define {
    public static final int GALLERY_FRAGMENT = 1;
    public static final int SEARCH_FRAGMENT = 2;
    public static final int SETTING_FRAGMENT = 3;
    public static final int DOWNLOAD_IMAGE_QUALITY = 100;
    public static final String DOWNLOAD_IMAGE_EXT = ".jpg";
    public static final String APP_FOLDER_NAME = "Wallpaper";
    public static final int CHANGE_CYCLE_SCREEN_OFF = -1;
    public static final int DELAY_MILLIS = 100;
    public static final int MINUTE_CONVERT_TO_MILLIS = 60*1000;
    public static final int HOUR_CONVERT_TO_MILLIS = 60*60*1000;
    public static final boolean USE_WALLPAPER = true;
    public static final boolean NOT_USE_WALLPAPER = false;
    public static final int DEFAULT_CHANGE_CYCLE = 0;
    public static final int DEFAULT_CHANGE_SCREEN_TYPE = R.string.label_wallpaper;
    public static final int[] CHANGE_SCREEN_TYPE = {
            R.string.label_wallpaper,
            R.string.label_lock_screen,
            R.string.label_wallpaper_plus_lock_screen
    };
    public static final int INDEX_TYPE_WALLPAPER = 0;
    public static final int INDEX_TYPE_LOCK_SCREEN = 1;
    public static final int INDEX_TYPE_WALLPAPER_PLUS_LOCK_SCREEN = 2;
    public static final int ID_ALARM_DEFAULT = 2100;
    public static final int ID_ALARM_TRANSPARENT = 9999;
    public static final int TYPE_USING_WALLPAPER = 304;
    public static final int TYPE_USING_RANDOM = 593;
    public static final int LIMIT_WIDTH_FOR_SAMPLE = 1980;
    public static final int LIMIT_HEIGHT_FOR_SAMPLE = 1980;
    public static final int LOW_SAMPLE_SIZE = 1;
    public static final int HIGH_SAMPLE_SIZE = 4;

    public static final int[] OPEN_SOURCE_LICENSE_DESCRIPTIONS = {
            R.raw.license_aosp,
            R.raw.license_aosp,
            R.raw.license_aosp,
            R.raw.license_realm,
            R.raw.license_jake_wharton,
            R.raw.license_square,
            R.raw.license_jack_wang,
            R.raw.license_akexorcist,
            R.raw.license_maxim_dybarsky,
            R.raw.license_chris_banes
    };
}
