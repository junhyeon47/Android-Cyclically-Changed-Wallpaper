package com.boostcamp.hyeon.wallpaper.gallery.adapter.holder;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.boostcamp.hyeon.wallpaper.R;

import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;
import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyeon on 2017. 2. 12..
 */

public class ImageListViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ImageListViewHolder.class.getSimpleName();
    @BindView(R.id.layout_item) FrameLayout mItemFrameLayout;
    @BindView(R.id.iv_thumbnail) ImageView mThumbnailImageView;
    @BindView(R.id.iv_select) ImageView mSelectImageView;

    private Context mContext;
    private OnItemClickListener mOnItemCLickListener;
    private int mSize;

    public ImageListViewHolder(View itemView, Context mContext, OnItemClickListener mOnItemCLickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = mContext;
        this.mOnItemCLickListener = mOnItemCLickListener;

        mSize = ((WallpaperApplication)mContext.getApplicationContext()).mDeviceWidthSize/9*2;

        ViewGroup.LayoutParams layoutParams = mItemFrameLayout.getLayoutParams();
        layoutParams.width = mSize;
        layoutParams.height = mSize;
        mItemFrameLayout.setLayoutParams(layoutParams);
    }

    public void bind(final Image image, final int position){
        Picasso.with(mContext)
                .load(Uri.parse(image.getThumbnailUri()))
                .rotate(Integer.valueOf(image.getOrientation()))
                .fit()
                .centerCrop()
                .into(mThumbnailImageView);

        if(image.getSelected()){
            mSelectImageView.setVisibility(View.VISIBLE);
        }else{
            mSelectImageView.setVisibility(View.GONE);
        }

        mThumbnailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_GALLEY_SELECT_MODE, false))
                    mOnItemCLickListener.onItemClick(position);
                else
                    mOnItemCLickListener.onItemClick(position);
            }
        });

        mThumbnailImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemCLickListener.onItemLongClick(position);
                return true;
            }
        });

        mSelectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemCLickListener.onItemClick(position);
            }
        });
    }
}
