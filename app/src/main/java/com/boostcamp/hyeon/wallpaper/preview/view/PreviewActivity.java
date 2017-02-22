package com.boostcamp.hyeon.wallpaper.preview.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;
import com.boostcamp.hyeon.wallpaper.preview.adapter.PreviewListAdapter;
import com.boostcamp.hyeon.wallpaper.preview.presenter.PreviewListPresenter;
import com.boostcamp.hyeon.wallpaper.preview.presenter.PreviewListPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.Sort;

public class PreviewActivity extends Activity implements PreviewListPresenter.View{
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private PreviewListAdapter mPreviewListAdapter;
    private PreviewListPresenterImpl mPreviewListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ButterKnife.bind(this);
        init();
    }

    private void init(){
        //init adapter
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        mPreviewListAdapter = new PreviewListAdapter(
                getApplicationContext(),
                realm.where(Image.class).equalTo("isSelected", true).findAllSorted("number", Sort.ASCENDING),
                true
        );
        realm.commitTransaction();

        //init presenter
        mPreviewListPresenter = new PreviewListPresenterImpl();
        mPreviewListPresenter.attachView(this);
        mPreviewListPresenter.setListAdapterModel(mPreviewListAdapter);
        mPreviewListPresenter.setListAdapterView(mPreviewListAdapter);

        //init recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mPreviewListAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.BOOLEAN_PREVIEW_ACTIVITY_CALL, false);
        super.onDestroy();
    }

    @OnClick(R.id.ib_back)
    public void back(){
        onBackPressed();
    }
}
