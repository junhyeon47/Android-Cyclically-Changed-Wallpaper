package com.boostcamp.hyeon.wallpaper.setting.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.domain.Wallpaper;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by hyeon on 2017. 2. 22..
 */

public class WallpaperListViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    private OnItemClickListener mOnItemCLickListener;
    private int mWidth;
    private int mHeight;
    @BindView(R.id.image_view) ImageView mImageView;
    @BindView(R.id.tv_number) TextView mNumberTextView;
    @BindView(R.id.layout_border) FrameLayout mBorderFrameLayout;
    @BindView(R.id.layout_number) LinearLayout mNumberLinearLayout;

    public WallpaperListViewHolder(View itemView, Context mContext, OnItemClickListener mOnItemCLickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = mContext;
        this.mOnItemCLickListener = mOnItemCLickListener;

        mWidth = ((WallpaperApplication)mContext.getApplicationContext()).mDeviceWidthSize/2;
        mHeight = ((WallpaperApplication)mContext.getApplicationContext()).mDeviceHeightSize/2;

        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
        layoutParams.width = mWidth;
        layoutParams.height = mHeight;
        mImageView.setLayoutParams(layoutParams);

        layoutParams = mBorderFrameLayout.getLayoutParams();
        layoutParams.width = mWidth;
        layoutParams.height = mHeight;
        mBorderFrameLayout.setLayoutParams(layoutParams);
    }

    public void bind(final Image image, final int position) {
        Picasso.with(mContext)
                .load(image.getImageUri())
                .resize(mWidth, mHeight)
                .centerCrop()
                .into(mImageView);

        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        Wallpaper wallpaper = realm.where(Wallpaper.class).findFirst();
        int currentPosition = wallpaper.getCurrentPosition();
        RealmList<Image> imageRealmList = wallpaper.getImages();
        realm.commitTransaction();
        if(imageRealmList.size() == 1){
            mBorderFrameLayout.setVisibility(View.GONE);
            mNumberLinearLayout.setVisibility(View.GONE);
        }else{
            mNumberLinearLayout.setVisibility(View.VISIBLE);
            int number = 0;
            for(Image item : imageRealmList){
                if(image.equals(item)){
                    number = imageRealmList.indexOf(item);
                    break;
                }
            }
            if(currentPosition == 0)
                currentPosition = imageRealmList.size()-1;
            else
                currentPosition -= 1;

            if(number == currentPosition){
                mBorderFrameLayout.setVisibility(View.VISIBLE);
            }else{
                mBorderFrameLayout.setVisibility(View.GONE);
            }
            mNumberTextView.setText(String.valueOf(number+1));
        }

    }
}
