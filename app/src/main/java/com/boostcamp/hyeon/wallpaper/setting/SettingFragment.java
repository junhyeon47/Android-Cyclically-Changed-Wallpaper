package com.boostcamp.hyeon.wallpaper.setting;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.domain.Wallpaper;
import com.boostcamp.hyeon.wallpaper.setting.adapter.WallpaperListAdapter;
import com.boostcamp.hyeon.wallpaper.setting.presenter.SettingPresenter;
import com.boostcamp.hyeon.wallpaper.setting.presenter.SettingPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements SettingPresenter.View{
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.tv_wallpaper_type) TextView mWallpaperTypeTextView;
    @BindView(R.id.tv_wallpaper_change_cycle) TextView mWallpaperChangeCycleTextView;
    @BindView(R.id.tv_random_wallpaper) TextView mRandomWallpaperTextView;
    @BindView(R.id.tv_transparent_wallpaper) TextView mTransparentWallpaperTextView;
    @BindView(R.id.sw_wallpaper_type) SwitchCompat mWallpaperTypeSwitch;
    @BindView(R.id.sw_wallpaper_change_cycle) SwitchCompat mWallpaperChangeSwitch;
    @BindView(R.id.sw_random_wallpaper) SwitchCompat mRandomWallpaperSwitch;
    @BindView(R.id.sw_transparent_wallpaper) SwitchCompat mTransparentSwitch;
    @BindView(R.id.layout_recycler_view) LinearLayout mRecyclerViewLinearLayout;
    private WallpaperListAdapter mAdapter;
    private SettingPresenterImpl mSettingPresenter;

    public SettingFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);

        init();
        return view;
    }

    private void init(){
        //init adapter
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();

        RealmResults<Image> imageRealmResults = null;
        Wallpaper wallpaper= realm.where(Wallpaper.class).findFirst();
        if(wallpaper != null){
            imageRealmResults = realm.where(Wallpaper.class).findFirst().getImages().sort("number", Sort.ASCENDING);
        }
        mAdapter = new WallpaperListAdapter(
                getContext(),
                imageRealmResults,
                true
        );
        realm.commitTransaction();

        //init presenter
        mSettingPresenter = new SettingPresenterImpl();
        mSettingPresenter.attachView(this);
        mSettingPresenter.setListAdapterModel(mAdapter);
        mSettingPresenter.setListAdapterView(mAdapter);

        //init recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));

        if(imageRealmResults == null){
            mRecyclerViewLinearLayout.setVisibility(View.GONE);
        }
    }
}
