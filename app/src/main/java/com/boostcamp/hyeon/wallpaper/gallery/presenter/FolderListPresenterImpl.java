package com.boostcamp.hyeon.wallpaper.gallery.presenter;


import com.boostcamp.hyeon.wallpaper.gallery.model.GalleryModel;
import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.contract.FolderListAdapterContract;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public class FolderListPresenterImpl implements FolderListPresenter.Presenter, OnItemClickListener {
    private FolderListPresenter.View mView;
    private FolderListAdapterContract.Model mAdapterModel;
    private FolderListAdapterContract.View mAdapterView;
    private GalleryModel mGalleryModel;

    public FolderListPresenterImpl(GalleryModel mGalleryModel) {
        this.mGalleryModel = mGalleryModel;
    }

    @Override
    public void attachView(FolderListPresenter.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
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
    public void onItemClick(int position) {
        mAdapterModel.update(position);
        mAdapterView.notifyAdapter();

        //adapter change
        mView.clickFolder(position);
    }

    @Override
    public void onItemLongClick(int position) {

    }
}
