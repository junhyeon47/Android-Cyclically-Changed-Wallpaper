package com.boostcamp.hyeon.wallpaper.gallery.presenter;

import android.content.Context;

import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.contract.FolderListAdapterContract;
import com.boostcamp.hyeon.wallpaper.gallery.model.FolderModel;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public class FolderListPresenterImpl implements FolderListPresenter.Presenter, OnItemClickListener {
    private FolderListPresenter.View mView;
    private FolderListAdapterContract.Model mAdapterModel;
    private FolderListAdapterContract.View mAdapterView;
    private FolderModel mFolderModel;

    @Override
    public void attachView(FolderListPresenter.View view) {
        this.mView = view;
        mFolderModel = new FolderModel();
    }

    @Override
    public void detachView() {

    }

    @Override
    public void setFolderListAdapterModel(FolderListAdapterContract.Model adapterModel) {
        this.mAdapterModel = adapterModel;
    }

    @Override
    public void setFolderListAdapterView(FolderListAdapterContract.View adapterView) {
        this.mAdapterView = adapterView;
        this.mAdapterView.setOnItemClickListener(this);
    }

    @Override
    public void loadFolderList(Context context) {
        mFolderModel.syncContentProviderToRealm(context);
    }

    @Override
    public void updateImageList() {
        mFolderModel.updateRealmObjectForDefaultMode();
    }

    @Override
    public void onItemClick(int bucketId) {
        mAdapterModel.update(bucketId);
        mAdapterView.notifyAdapter();
    }

    @Override
    public void onItemLongClick(int position) {

    }
}
