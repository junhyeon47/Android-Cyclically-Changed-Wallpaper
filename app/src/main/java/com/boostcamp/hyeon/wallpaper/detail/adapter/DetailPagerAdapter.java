package com.boostcamp.hyeon.wallpaper.detail.adapter;

import android.content.Context;
import android.support.v4.util.Pools;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by hyeon on 2017. 2. 19..
 */

public class DetailPagerAdapter extends PagerAdapter {
    private static final String TAG = DetailPagerAdapter.class.getSimpleName();
    private static final int MAX_POOL_SIZE = 10;
    private Context mContext;
    private RealmResults<Image> mList;
    private Pools.SimplePool<View> mPool;
    private PhotoViewAttacher mAttacher;
    @BindView(R.id.image_view) ImageView mImageView;

    public DetailPagerAdapter(Context mContext, RealmResults<Image> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mPool = new Pools.SynchronizedPool<>(MAX_POOL_SIZE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getPagerItemView();

        ButterKnife.bind(this, view);

        Image image = mList.get(position);
        Picasso.with(mContext)
                .load(image.getImageUri())
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mAttacher = new PhotoViewAttacher(mImageView);
                    }

                    @Override
                    public void onError() {

                    }
                });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mAttacher = null;
        mPool.release((View)object);
        container.removeView((View)object);
    }

    private View getPagerItemView() {
        View view = mPool.acquire();
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_detail, null);
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
}
