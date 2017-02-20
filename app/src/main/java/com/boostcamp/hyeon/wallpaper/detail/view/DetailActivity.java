package com.boostcamp.hyeon.wallpaper.detail.view;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.detail.adapter.DetailPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class DetailActivity extends Activity {
    private static final String TAG = DetailActivity.class.getSimpleName();
    @BindView(R.id.view_pager) ViewPager mViewPager;
    private DetailPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        init();
    }

    public void init(){
        Bundle bundle = getIntent().getExtras();

        String bucketId = bundle.getString("bucket_id");
        String imageId = bundle.getString("image_id");

        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        RealmResults<Image> imageRealmResults = realm.where(Image.class).equalTo("bucketId", bucketId).findAll();
        realm.commitTransaction();
        int position = 0;

        for(Image image : imageRealmResults){
            if(image.getImageId().equals(imageId)){
                position = imageRealmResults.indexOf(image);
            }
        }

        mAdapter = new DetailPagerAdapter(getApplicationContext(), imageRealmResults);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(position);
    }

    @OnClick(R.id.ib_back)
    public void back(){
        onBackPressed();
    }

}
