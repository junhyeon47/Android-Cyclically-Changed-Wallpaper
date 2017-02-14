package com.boostcamp.hyeon.wallpaper.gallery.adapter.holder;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.gallery.model.Image;
import com.bumptech.glide.Glide;

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

    public ImageListViewHolder(View itemView, Context mContext, OnItemClickListener mOnItemCLickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = mContext;
        this.mOnItemCLickListener = mOnItemCLickListener;

        int size = ((WallpaperApplication)mContext.getApplicationContext()).mDeviceWidthSize/4;

        ViewGroup.LayoutParams layoutParams = mItemFrameLayout.getLayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
        mItemFrameLayout.setLayoutParams(layoutParams);
    }

    public void bind(final Image image, int position){
        Glide.with(mContext)
                .loadFromMediaStore(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image.getImageId()))
                .centerCrop()
                .crossFade()
                .into(mThumbnailImageView);

        if(image.getSelected()){
            mSelectImageView.setVisibility(View.VISIBLE);
        }else{
            mSelectImageView.setVisibility(View.GONE);
        }

        mThumbnailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelectMode())
                    mOnItemCLickListener.onItemClick(image.getImageId());
                else
                    mOnItemCLickListener.onItemClick(-1);
            }
        });

        mThumbnailImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemCLickListener.onItemLongClick(image.getImageId());
                return true;
            }
        });

        mSelectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemCLickListener.onItemClick(image.getImageId());
            }
        });
    }

    private boolean isSelectMode(){
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.pref_gallery), Context.MODE_PRIVATE);
        return preferences.getBoolean(mContext.getString(R.string.pref_gallery_select_key), false);
    }
}
