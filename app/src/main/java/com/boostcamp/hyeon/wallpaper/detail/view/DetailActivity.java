package com.boostcamp.hyeon.wallpaper.detail.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.domain.ImageNaver;
import com.boostcamp.hyeon.wallpaper.base.util.Define;
import com.boostcamp.hyeon.wallpaper.detail.adapter.DetailPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class DetailActivity extends Activity {
    private static final String TAG = DetailActivity.class.getSimpleName();
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.ib_download) ImageButton mDownloadImageButton;
    private DetailPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        init();
    }

    public void init(){
        RealmResults realmResults = null;
        int position = 0;
        Bundle bundle = getIntent().getExtras();
        int from = bundle.getInt("from");
        if(from == Define.GALLERY_FRAGMENT) {
            mDownloadImageButton.setVisibility(View.GONE);
            String bucketId = bundle.getString("bucket_id");
            String imageId = bundle.getString("image_id");

            Realm realm = WallpaperApplication.getRealmInstance();
            realm.beginTransaction();
            realmResults = realm.where(Image.class).equalTo("bucketId", bucketId).findAll();
            realm.commitTransaction();

            for (Object object : realmResults) {
                if(object instanceof Image) {
                    Image image = (Image)object;
                    if (image.getImageId().equals(imageId)) {
                        position = realmResults.indexOf(image);
                    }
                }
            }
        }else if(from == Define.SEARCH_FRAGMENT){
            mDownloadImageButton.setVisibility(View.VISIBLE);
            String link = bundle.getString("link");

            Realm realm = WallpaperApplication.getRealmInstance();
            realm.beginTransaction();
            realmResults = realm.where(ImageNaver.class).findAll();
            realm.commitTransaction();

            for (Object object : realmResults) {
                if(object instanceof ImageNaver) {
                    ImageNaver imageNaver = (ImageNaver)object;
                    if (imageNaver.getLink().equals(link)) {
                        position = realmResults.indexOf(imageNaver);
                    }
                }
            }
        }else{
            //error handling
            finish();
        }

        mAdapter = new DetailPagerAdapter(this, realmResults, from);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(position);
    }

    @OnClick(R.id.ib_back)
    public void onClickBack(){
        onBackPressed();
    }

    @OnClick(R.id.ib_download)
    public void onClickDownload(){
        mAdapter.downloadImage(mViewPager.getCurrentItem());
    }
}
