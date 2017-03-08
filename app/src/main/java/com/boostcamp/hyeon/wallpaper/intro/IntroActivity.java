package com.boostcamp.hyeon.wallpaper.intro;

import android.os.Bundle;

import com.boostcamp.hyeon.wallpaper.base.util.Define;
import com.boostcamp.hyeon.wallpaper.base.util.IntroSlide;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;

import agency.tango.materialintroscreen.MaterialIntroActivity;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.BOOLEAN_FIRST_EXECUTION, false);
        for(int i=0; i<Define.NUMBER_OF_INTRO; ++i){
            IntroSlide introSlide = IntroSlide.newInstance(i);
            addSlide(introSlide);
        }
    }
}
