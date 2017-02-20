package com.boostcamp.hyeon.wallpaper.gallery.presenter;

import android.os.Bundle;

import com.boostcamp.hyeon.wallpaper.base.adapter.RealmListAdapterContract;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public interface ImageListPresenter {
    interface View{
        void changeModeForSelect();
        void moveToDetailActivity(Bundle bundle);
    }
    interface Presenter{
        void attachView(View view);
        void detachView();
        void setListAdapterModel(RealmListAdapterContract.Model<Image> adapterModel);
        void setListAdapterView(RealmListAdapterContract.View adapterView);
        void updateAllImagesDeselected();
    }
}
