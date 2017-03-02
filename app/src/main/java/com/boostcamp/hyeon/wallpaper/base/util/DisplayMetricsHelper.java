package com.boostcamp.hyeon.wallpaper.base.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by hyeon on 2017. 2. 16..
 */

public class DisplayMetricsHelper {
    private DisplayMetrics mDisplayMetrics;

    private DisplayMetricsHelper() {
    }

    private static class DisplayMetricHelperHolder{
        public static final DisplayMetricsHelper INSTANCE = new DisplayMetricsHelper();
    }

    public static DisplayMetricsHelper getInstance(Context context){
        DisplayMetricsHelper instance = DisplayMetricHelperHolder.INSTANCE;
        instance.mDisplayMetrics = context.getResources().getDisplayMetrics();
        return instance;
    }

    public static DisplayMetricsHelper getInstance(){
        DisplayMetricsHelper instance = DisplayMetricHelperHolder.INSTANCE;
        if(instance.mDisplayMetrics != null){
            return DisplayMetricHelperHolder.INSTANCE;
        }
        throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");
    }

    public int getDeviceWidth(){
        return mDisplayMetrics.widthPixels;
    }

    public int getDeviceHeight(){
        return mDisplayMetrics.heightPixels;
    }
}
