package com.boostcamp.hyeon.wallpaper.detail.adapter;

import android.app.Activity;
import android.support.v4.util.Pools;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.domain.ImageNaver;
import com.boostcamp.hyeon.wallpaper.base.util.Define;
import com.boostcamp.hyeon.wallpaper.base.util.ImageDownloadAsyncTask;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;


/**
 * Created by hyeon on 2017. 2. 19..
 */

public class DetailPagerAdapter extends PagerAdapter {
    private static final String TAG = DetailPagerAdapter.class.getSimpleName();
    private static final int MAX_POOL_SIZE = 3;
    private Activity mActivity;
    private RealmResults mList;
    private Pools.SimplePool<View> mPool;
    private int mFrom;
    @BindView(R.id.image_view) ImageView mImageView;
    @BindView(R.id.avl_loading) AVLoadingIndicatorView mAvLoadingIndicatorView;

    public DetailPagerAdapter(Activity mActivity, RealmResults mList, int mFrom) {
        this.mActivity = mActivity;
        this.mList = mList;
        this.mFrom = mFrom;
        this.mPool = new Pools.SynchronizedPool<>(MAX_POOL_SIZE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem");
        View view = getPagerItemView();
        ButterKnife.bind(this, view);

        String imageUri;
        if(mFrom == Define.GALLERY_FRAGMENT) {
            imageUri = ((Image)mList.get(position)).getImageUri();
            Picasso.with(mActivity)
                    .load(new File(imageUri))
                    .fit()
                    .centerInside()
                    .into(mImageView);
        }else if(mFrom == Define.SEARCH_FRAGMENT){
            imageUri = ((ImageNaver)mList.get(position)).getLink();
            Picasso.with(mActivity)
                    .load(imageUri)
                    .fit()
                    .centerInside()
                    .into(mImageView);
        }

        mAvLoadingIndicatorView.show();

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mPool.release((View)object);
        container.removeView((View)object);
    }

    private View getPagerItemView() {
        View view = mPool.acquire();
        if (view == null) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_detail, null);
        }

        return view;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    public void downloadImage(int position) {
        String imageUri = ((ImageNaver) mList.get(position)).getLink();
        new ImageDownloadAsyncTask(mActivity).execute(imageUri);
    }
}
