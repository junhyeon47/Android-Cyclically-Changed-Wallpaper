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
        void clickFolder(int position);
    }
    interface Presenter{
        void attachView(View view);
        void detachView();
        void setFolderListAdapterModel(FolderListAdapterContract.Model adapterModel);
        void setFolderListAdapterView(FolderListAdapterContract.View adapterView);
    }
}
