package com.boostcamp.hyeon.wallpaper.gallery.presenter;

import android.content.Context;

import com.boostcamp.hyeon.wallpaper.gallery.adapter.contract.FolderListAdapterContract;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public interface FolderListPresenter {
    interface View{
        void changeModeForSelect();
        void changeModeForDefault();
    }
    interface Presenter{
        void attachView(FolderListPresenter.View view);
        void detachView();
        void setFolderListAdapterModel(FolderListAdapterContract.Model adapterModel);
        void setFolderListAdapterView(FolderListAdapterContract.View adapterView);
        void loadFolderList(Context context);
        void updateImageList();
    }
}
