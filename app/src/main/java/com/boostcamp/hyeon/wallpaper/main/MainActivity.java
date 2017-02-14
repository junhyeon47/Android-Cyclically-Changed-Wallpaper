package com.boostcamp.hyeon.wallpaper.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.listener.OnBackKeyPressedListener;
import com.boostcamp.hyeon.wallpaper.main.adapter.ViewPagerAdapter;
import com.boostcamp.hyeon.wallpaper.search.SearchFragment;
import com.boostcamp.hyeon.wallpaper.gallery.GalleryFragment;
import com.boostcamp.hyeon.wallpaper.setting.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private static final int PERMISSION_GALLERY = 394;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;

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

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }else{
            init();
        }
    }

    private void init(){
        //create ViewPagerAdapter, add Fragment, mViewPager apply adapter.
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new SearchFragment());
        adapter.addFragment(new SettingFragment());
        mViewPager.setAdapter(adapter);

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
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                }else{
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                break;
        }
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_GALLERY);
        }else{
            init();
        }
    }
}
