package com.boostcamp.hyeon.wallpaper.base.util;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager.LayoutParams;

import com.boostcamp.hyeon.wallpaper.R;

public class TransparentActivity extends Activity {
    private static final String TAG = TransparentActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        getWindow().addFlags(LayoutParams.FLAG_TURN_SCREEN_ON | LayoutParams.FLAG_SHOW_WHEN_LOCKED | LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_transparent);
        finish();
        Log.d(TAG, "finish");
    }
}
