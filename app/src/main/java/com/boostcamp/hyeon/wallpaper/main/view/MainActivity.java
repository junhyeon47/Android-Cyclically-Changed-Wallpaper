package com.boostcamp.hyeon.wallpaper.main.view;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.listener.OnBackKeyPressedListener;
import com.boostcamp.hyeon.wallpaper.base.service.TransparentActivityCallService;
import com.boostcamp.hyeon.wallpaper.main.adapter.ViewPagerAdapter;
import com.boostcamp.hyeon.wallpaper.search.SearchFragment;
import com.boostcamp.hyeon.wallpaper.gallery.view.GalleryFragment;
import com.boostcamp.hyeon.wallpaper.setting.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;
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
        Intent intent = new Intent(this, TransparentActivityCallService.class);
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
        getSupportActionBar().setTitle(mToolbarTitles[tab.getPosition()]);
        supportInvalidateOptionsMenu();

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
