package com.boostcamp.hyeon.wallpaper.main.view;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.listener.OnBackKeyPressedListener;
import com.boostcamp.hyeon.wallpaper.base.service.WallpaperManagerService;
import com.boostcamp.hyeon.wallpaper.main.adapter.ViewPagerAdapter;
import com.boostcamp.hyeon.wallpaper.search.view.SearchFragment;
import com.boostcamp.hyeon.wallpaper.gallery.view.GalleryFragment;
import com.boostcamp.hyeon.wallpaper.setting.view.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.et_search) EditText mSearchEditText;
    @BindView(R.id.iv_search_icon) ImageView mSearchIconImageView;

    private ViewPagerAdapter mAdapter;
    private OnBackKeyPressedListener mOnBackKeyPressedListener;

    private int mTabIcons[] = new int[]{
            R.drawable.selector_tab_btn_gallery,
            R.drawable.selector_tab_btn_search,
            R.drawable.selector_tab_btn_setting
    };

    private int mToolbarTitles[] = new int[]{
            R.string.title_gallery,
            R.string.title_search,
            R.string.title_setting
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        //init service and start
        Intent intent = new Intent(this, WallpaperManagerService.class);
        startService(intent);

        //create ViewPagerAdapter, add Fragment, mViewPager apply adapter.
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new GalleryFragment());
        mAdapter.addFragment(new SearchFragment());
        mAdapter.addFragment(new SettingFragment());
        mViewPager.setAdapter(mAdapter);

        //mTabLayout setup mViewPager
        mTabLayout.setupWithViewPager(mViewPager);

        //add icons each tab.
        for(int i=0; i<mTabLayout.getTabCount(); ++i){
            mTabLayout.getTabAt(i).setIcon(mTabIcons[i]);
        }

        //add EventListener to mTabLayout.
        mTabLayout.addOnTabSelectedListener(this);

        //setup ActionBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mToolbarTitles[mTabLayout.getSelectedTabPosition()]);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //ActionBar title change.
        supportInvalidateOptionsMenu();
        if(mToolbarTitles[tab.getPosition()] == R.string.title_search) {
            getSupportActionBar().setTitle(null);
            mSearchEditText.setVisibility(View.VISIBLE);
            mSearchIconImageView.setVisibility(View.VISIBLE);
        }else{
            getSupportActionBar().setTitle(mToolbarTitles[tab.getPosition()]);
            mSearchEditText.setVisibility(View.GONE);
            mSearchIconImageView.setVisibility(View.GONE);
        }

        //life cycle change
        mAdapter.getItem(tab.getPosition()).onResume();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //life cycle change
        mAdapter.getItem(tab.getPosition()).onPause();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void setOnBackKeyPressedListener(OnBackKeyPressedListener listener){
        this.mOnBackKeyPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if(mOnBackKeyPressedListener != null){
            mOnBackKeyPressedListener.onBack();
        }else {
            super.onBackPressed();
        }
    }
}
