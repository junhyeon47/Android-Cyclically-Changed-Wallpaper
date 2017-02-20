package com.boostcamp.hyeon.wallpaper.preview.presenter;

import com.boostcamp.hyeon.wallpaper.base.adapter.RealmListAdapterContract;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;

/**
 * Created by hyeon on 2017. 2. 20..
 */

public interface PreviewListPresenter {
    interface View{
    }
    interface Presenter{
        void attachView(View view);
        void detachView();
        void setListAdapterModel(RealmListAdapterContract.Model<Image> adapterModel);
        void setListAdapterView(RealmListAdapterContract.View adapterView);
    }
}
