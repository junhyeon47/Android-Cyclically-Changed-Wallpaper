package com.boostcamp.hyeon.wallpaper.search.presenter;

import android.os.Bundle;

import com.boostcamp.hyeon.wallpaper.base.adapter.RealmListAdapterContract;
import com.boostcamp.hyeon.wallpaper.base.domain.ImageNaver;

/**
 * Created by hyeon on 2017. 2. 21..
 */

public interface ImageNaverListPresenter {
    interface View{
        void moveToDetailActivity(Bundle bundle);
    }
    interface Presenter{
        void attachView(ImageNaverListPresenter.View view);
        void detachView();
        void setListAdapterModel(RealmListAdapterContract.Model<ImageNaver> adapterModel);
        void setListAdapterView(RealmListAdapterContract.View adapterView);
        void search(String query, int start);
    }
}
