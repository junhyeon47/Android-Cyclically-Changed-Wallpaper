package com.boostcamp.hyeon.wallpaper.search.presenter;

import android.content.Context;
import android.os.Bundle;

import com.boostcamp.hyeon.wallpaper.base.adapter.RealmListAdapterContract;
import com.boostcamp.hyeon.wallpaper.base.domain.ImageNaver;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.base.util.Define;
import com.boostcamp.hyeon.wallpaper.search.model.SearchModel;

/**
 * Created by hyeon on 2017. 2. 21..
 */

public class ImageNaverListPresenterImpl implements ImageNaverListPresenter.Presenter, SearchModel.ModelDataChange, OnItemClickListener {
    private ImageNaverListPresenter.View mView;
    private RealmListAdapterContract.Model<ImageNaver> mAdapterModel;
    private RealmListAdapterContract.View mAdapterView;
    private SearchModel mSearchModel;

    public ImageNaverListPresenterImpl(Context context) {
        mSearchModel = new SearchModel(context);
    }

    @Override
    public void attachView(ImageNaverListPresenter.View view) {
        this.mView = view;
        mSearchModel.setOnChangeListener(this);
    }

    @Override
    public void detachView() {
        this.mView = null;
        this.mSearchModel = null;
    }

    @Override
    public void setListAdapterModel(RealmListAdapterContract.Model<ImageNaver> adapterModel) {
        this.mAdapterModel = adapterModel;
    }

    @Override
    public void setListAdapterView(RealmListAdapterContract.View adapterView) {
        this.mAdapterView = adapterView;
        this.mAdapterView.setOnItemClickListener(this);
    }

    @Override
    public void search(String query, int start) {
        mSearchModel.callGetList(query, start);
    }

    @Override
    public void onItemClick(int position) {
        String link = mAdapterModel.getItem(position).getLink();


        Bundle bundle = new Bundle();
        bundle.putInt("from", Define.SEARCH_FRAGMENT);
        bundle.putString("link", link);

        mView.moveToDetailActivity(bundle);
    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void update() {
        mAdapterView.notifyAdapter();
    }

    @Override
    public void fail() {
        mView.showFailureMessage();
    }
}
