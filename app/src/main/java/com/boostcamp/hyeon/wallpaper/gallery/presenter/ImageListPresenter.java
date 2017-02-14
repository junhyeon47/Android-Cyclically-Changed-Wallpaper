package com.boostcamp.hyeon.wallpaper.gallery.presenter;

import android.content.Context;

import com.boostcamp.hyeon.wallpaper.gallery.adapter.contract.ImageListAdapterContract;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public interface ImageListPresenter {
    interface View{
        void changeModeForSelect();
    }
    interface Presenter{
        void attachView(ImageListPresenter.View view);
        void detachView();
        void setImageListAdapterModel(ImageListAdapterContract.Model adapterModel);
        void setImageListAdapterView(ImageListAdapterContract.View adapterView);
    }
}
