package com.boostcamp.hyeon.wallpaper.gallery.presenter;

import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.contract.ImageListAdapterContract;
import com.boostcamp.hyeon.wallpaper.gallery.model.ImageModel;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public class ImageListPresenterImpl implements ImageListPresenter.Presenter, OnItemClickListener {
    private ImageListPresenter.View mView;
    private ImageListAdapterContract.Model mAdapterModel;
    private ImageListAdapterContract.View mAdapterView;
    private ImageModel mImageModel;

    @Override
    public void attachView(ImageListPresenter.View view) {
        this.mView = view;
        mImageModel = new ImageModel();
    }

    @Override
    public void detachView() {

    }

    @Override
    public void setImageListAdapterModel(ImageListAdapterContract.Model adapterModel) {
        this.mAdapterModel = adapterModel;
    }

    @Override
    public void setImageListAdapterView(ImageListAdapterContract.View adapterView) {
        this.mAdapterView = adapterView;
        this.mAdapterView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int imageId) {
        if(imageId == -1){
            //moveToDetailActivity
        }else{
            mAdapterModel.update(imageId);
            mAdapterView.notifyAdapter();
        }
    }

    @Override
    public void onItemLongClick(int imageId) {
        mView.changeModeForSelect();
        //realm image object update. position -> select
        mAdapterModel.update(imageId);
        mAdapterView.notifyAdapter();
    }

}
