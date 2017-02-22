package com.boostcamp.hyeon.wallpaper.setting.presenter;

import com.boostcamp.hyeon.wallpaper.base.adapter.RealmListAdapterContract;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;

/**
 * Created by hyeon on 2017. 2. 22..
 */

public interface SettingPresenter {
    interface View{
    }
    interface Presenter{
        void attachView(SettingPresenter.View view);
        void detachView();
        void setListAdapterModel(RealmListAdapterContract.Model<Image> adapterModel);
        void setListAdapterView(RealmListAdapterContract.View adapterView);
    }
}
