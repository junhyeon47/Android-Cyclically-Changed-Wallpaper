package com.boostcamp.hyeon.wallpaper.base.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hyeon on 2017. 2. 16..
 */

public class SharedPreferenceHelper {
    private static final String TAG = SharedPreferenceHelper.class.getSimpleName();
    private static final String SETTINGS_NAME = "wallpaper_setting";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public enum Key{
        BOOLEAN_FIRST_INIT("pref_key_first_init"),
        BOOLEAN_GALLEY_SELECT_MODE("pref_key_gallery_select_mode"),
        STRING_SYNC_DATE("pref_key_sync_date");

        private final String name;

        Key(String s) {
            name = s;
        }

        public String getName() {
            return name;
        }
    }

    private SharedPreferenceHelper() {
    }

    private static class SharedPreferenceHelperHolder{
        public static final SharedPreferenceHelper INSTANCE = new SharedPreferenceHelper();
    }

    public static SharedPreferenceHelper getInstance(Context context){
        SharedPreferenceHelper instance = SharedPreferenceHelperHolder.INSTANCE;
        instance.mSharedPreferences = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
        return instance;
    }

    public static SharedPreferenceHelper getInstance(){
        SharedPreferenceHelper instance = SharedPreferenceHelperHolder.INSTANCE;
        if(instance.mSharedPreferences != null){
            return SharedPreferenceHelperHolder.INSTANCE;
        }
        throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");
    }

    public void put(Key key, String value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putString(key.getName(), value);
        mEditor.apply();
    }

    public void put(Key key, int value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putInt(key.getName(), value);
        mEditor.apply();
    }

    public void put(Key key, boolean value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(key.getName(), value);
        mEditor.apply();
    }

    public void put(Key key, float value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putFloat(key.getName(), value);
        mEditor.apply();
    }

    public void put(Key key, double value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putString(key.getName(), String.valueOf(value));
        mEditor.apply();
    }

    public void put(Key key, long value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putLong(key.getName(), value);
        mEditor.apply();
    }


    public String getString(Key key, String defaultValue) {
        return mSharedPreferences.getString(key.getName(), defaultValue);
    }
    public int getInt(Key key, int defaultValue) {
        return mSharedPreferences.getInt(key.getName(), defaultValue);
    }

    public long getLong(Key key, long defaultValue) {
        return mSharedPreferences.getLong(key.getName(), defaultValue);
    }

    public float getFloat(Key key, float defaultValue) {
        return mSharedPreferences.getFloat(key.getName(), defaultValue);
    }

    public double getDouble(Key key, double defaultValue) {
        try {
            return Double.valueOf(mSharedPreferences.getString(key.getName(), String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(Key key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key.getName(), defaultValue);
    }
}
