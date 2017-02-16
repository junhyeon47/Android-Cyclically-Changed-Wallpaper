package com.boostcamp.hyeon.wallpaper.gallery.presenter;

import com.boostcamp.hyeon.wallpaper.gallery.model.GalleryModel;
import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.contract.ImageListAdapterContract;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public class ImageListPresenterImpl implements ImageListPresenter.Presenter, OnItemClickListener {
    private ImageListPresenter.View mView;
    private ImageListAdapterContract.Model mAdapterModel;
    private ImageListAdapterContract.View mAdapterView;
    private GalleryModel mGalleryModel;

    public ImageListPresenterImpl(GalleryModel mGalleryModel) {
        this.mGalleryModel = mGalleryModel;
    }

    @Override
    public void attachView(ImageListPresenter.View view) {
        this.mView = view;
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
    public void onItemClick(int position) {
        if(position == -1){
            //moveToDetailActivity
        }else{
            //if select mode
            mAdapterModel.update(position);
            mAdapterView.notifyAdapter(position);
        }
    }

    @Override
    public void onItemLongClick(int position) {
        mView.changeModeForSelect();
        //realm image object update. position -> select
        mAdapterModel.update(position);
        mAdapterView.notifyAdapter(position);
    }

}
