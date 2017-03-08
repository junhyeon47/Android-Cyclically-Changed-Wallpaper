package com.boostcamp.hyeon.wallpaper.base.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import agency.tango.materialintroscreen.SlideFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyeon on 2017. 3. 7..
 */

public class IntroSlide extends SlideFragment {
    @BindView(R.id.iv_intro) ImageView mIntroImageView;
    @BindView(R.id.tv_message) TextView mMessageTextView;

    public static IntroSlide newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt("index", index);
        IntroSlide fragment = new IntroSlide();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_intro_slide, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.colorIntroActivityBackground;
    }

    @Override
    public int buttonsColor() {
        return R.color.colorTeal300;
    }

    @Override
    public boolean canMoveFurther() {
        return super.canMoveFurther();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return super.cantMoveFurtherErrorMessage();
    }

    public void init(){
        int index = getArguments().getInt("index");

        Glide.with(getContext())
                .load(Define.INTRO_IMAGE[index])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mIntroImageView);

        mMessageTextView.setText(getString(Define.INTRO_MESSAGE[index]));
    }
}
