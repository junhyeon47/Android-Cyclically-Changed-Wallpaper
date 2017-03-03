package com.boostcamp.hyeon.wallpaper.gallery.presenter;


import com.boostcamp.hyeon.wallpaper.base.adapter.RealmListAdapterContract;
import com.boostcamp.hyeon.wallpaper.base.domain.Folder;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.gallery.model.GalleryModel;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public class FolderListPresenterImpl implements FolderListPresenter.Presenter, OnItemClickListener {
    private FolderListPresenter.View mView;
    private RealmListAdapterContract.Model<Folder> mAdapterModel;
    private RealmListAdapterContract.View mAdapterView;
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
    public void setListAdapterModel(RealmListAdapterContract.Model<Folder> adapterModel) {
        this.mAdapterModel = adapterModel;
    }

    @Override
    public void setListAdapterView(RealmListAdapterContract.View adapterView) {
        this.mAdapterView = adapterView;
        this.mAdapterView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(int position) {
        String bucketId = mAdapterModel.getItem(position).getBucketId();
        mGalleryModel.selectFolder(bucketId);
        mAdapterView.notifyAdapter();
        //adapter change
        mView.clickFolder(position);
    }

    @Override
    public void onItemLongClick(int position) {

    }
}
