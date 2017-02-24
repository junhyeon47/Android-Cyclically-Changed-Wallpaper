package com.boostcamp.hyeon.wallpaper.detail.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.util.Pools;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.domain.ImageNaver;
import com.boostcamp.hyeon.wallpaper.base.util.Define;
import com.boostcamp.hyeon.wallpaper.base.util.ImageDownloadAsyncTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.boostcamp.hyeon.wallpaper.base.util.Define.DOWNLOAD_IMAGE_EXT;
import static com.boostcamp.hyeon.wallpaper.base.util.Define.DOWNLOAD_IMAGE_QUALITY;


/**
 * Created by hyeon on 2017. 2. 19..
 */

public class DetailPagerAdapter extends PagerAdapter {
    private static final String TAG = DetailPagerAdapter.class.getSimpleName();
    private static final int MAX_POOL_SIZE = 10;
    private Activity mActivity;
    private RealmResults mList;
    private Pools.SimplePool<View> mPool;
    private PhotoViewAttacher mAttacher;
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
        View view = getPagerItemView();

        ButterKnife.bind(this, view);

        String imageUri = null;
        if(mFrom == Define.GALLERY_FRAGMENT) {
            imageUri = ((Image)mList.get(position)).getImageUri();
        }else if(mFrom == Define.SEARCH_FRAGMENT){
            imageUri = ((ImageNaver)mList.get(position)).getLink();
        }

        mAvLoadingIndicatorView.show();

        Picasso.with(mActivity)
                .load(imageUri)
                .into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        //sometimes error occur
                        //mAttacher = new PhotoViewAttacher(mImageView);
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
