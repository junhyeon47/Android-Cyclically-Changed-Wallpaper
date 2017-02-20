package com.boostcamp.hyeon.wallpaper.gallery.presenter;


import com.boostcamp.hyeon.wallpaper.base.adapter.RealmListAdapterContract;
import com.boostcamp.hyeon.wallpaper.base.domain.Folder;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public interface FolderListPresenter {
    interface View{
        void changeModeForSelect();
        void changeModeForDefault();
        void clickFolder(int position);
    }
    interface Presenter{
        void attachView(View view);
        void detachView();
        void setListAdapterModel(RealmListAdapterContract.Model<Folder> adapterModel);
        void setListAdapterView(RealmListAdapterContract.View adapterView);
    }
}
