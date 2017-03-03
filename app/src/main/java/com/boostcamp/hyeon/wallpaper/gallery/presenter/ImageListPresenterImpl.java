package com.boostcamp.hyeon.wallpaper.gallery.presenter;

import android.os.Bundle;

import com.boostcamp.hyeon.wallpaper.base.adapter.RealmListAdapterContract;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.base.util.Define;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;
import com.boostcamp.hyeon.wallpaper.gallery.model.GalleryModel;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public class ImageListPresenterImpl implements ImageListPresenter.Presenter, OnItemClickListener {
    private ImageListPresenter.View mView;
    private RealmListAdapterContract.Model<Image> mAdapterModel;
    private RealmListAdapterContract.View mAdapterView;
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
        this.mView = null;
    }

    @Override
    public void setListAdapterModel(RealmListAdapterContract.Model<Image> adapterModel) {
        this.mAdapterModel = adapterModel;
    }

    @Override
    public void setListAdapterView(RealmListAdapterContract.View adapterView) {
        this.mAdapterView = adapterView;
        this.mAdapterView.setOnItemClickListener(this);
    }

    @Override
    public void updateAllImagesDeselected() {
        mGalleryModel.updateAllImagesDeselected();
        mAdapterView.notifyAdapter();
    }

    @Override
    public void onItemClick(int position) {
        if(SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_GALLEY_SELECT_MODE, false)){
            //if select mode
            String imageId = mAdapterModel.getItem(position).getImageId();
            mGalleryModel.selectImage(imageId);
            mAdapterView.notifyAdapter(position);
        }else{
            //moveToDetailActivity
            String bucketId = mAdapterModel.getItem(position).getBucketId();
            String imageId = mAdapterModel.getItem(position).getImageId();

            Bundle bundle = new Bundle();
            bundle.putInt("from", Define.GALLERY_FRAGMENT);
            bundle.putString("bucket_id", bucketId);
            bundle.putString("image_id", imageId);
            mView.moveToDetailActivity(bundle);
        }
    }

    @Override
    public void onItemLongClick(int position) {
        mView.changeModeForSelect();
        //realm image object update. position -> select
        mAdapterView.notifyAdapter(position);
        String imageId = mAdapterModel.getItem(position).getImageId();
        mGalleryModel.selectImage(imageId);
        mAdapterView.notifyAdapter(position);
    }

}
