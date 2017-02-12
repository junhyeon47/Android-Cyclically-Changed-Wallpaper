package com.boostcamp.hyeon.wallpaper.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.main.adapter.ViewPagerAdapter;
import com.boostcamp.hyeon.wallpaper.search.SearchFragment;
import com.boostcamp.hyeon.wallpaper.select.SelectFragment;
import com.boostcamp.hyeon.wallpaper.setting.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;

    private int mTabIcons[] = new int[]{
            R.drawable.selector_tab_btn_select,
            R.drawable.selector_tab_btn_search,
            R.drawable.selector_tab_btn_setting
    };

    private int mToolbarTitles[] = new int[]{
            R.string.title_select,
            R.string.title_search,
            R.string.title_setting
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        //create ViewPagerAdapter, add Fragment, mViewPager apply adapter.
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SelectFragment());
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
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
