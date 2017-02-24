package com.boostcamp.hyeon.wallpaper.gallery.adapter.holder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;

import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;

import com.squareup.picasso.MemoryPolicy;
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
    @BindView(R.id.layout_select) RelativeLayout mSelectRelativeLayout;
    @BindView(R.id.tv_number) TextView mNumberTextView;

    private Context mContext;
    private OnItemClickListener mOnItemCLickListener;

    public ImageListViewHolder(View itemView, Context mContext, OnItemClickListener mOnItemCLickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = mContext;
        this.mOnItemCLickListener = mOnItemCLickListener;

        int size = ((WallpaperApplication)mContext.getApplicationContext()).mDeviceWidthSize/9*2;

        ViewGroup.LayoutParams layoutParams = mItemFrameLayout.getLayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
        mItemFrameLayout.setLayoutParams(layoutParams);
        Log.d(TAG, "ImageListViewHolder constructor");
    }

    public void bind(Image image, final int position){
        Picasso.with(mContext)
                .load(Uri.parse(image.getThumbnailUri()))
                .rotate(Integer.valueOf(image.getOrientation()))
                .fit()
                .centerCrop()
                .into(mThumbnailImageView);

        if(image.getSelected()){
            mSelectImageView.setVisibility(View.VISIBLE);
            mSelectRelativeLayout.setVisibility(View.VISIBLE);
            mNumberTextView.setText(String.valueOf(image.getNumber()));
        }else{
            mSelectImageView.setVisibility(View.GONE);
            mSelectRelativeLayout.setVisibility(View.GONE);
        }

        mThumbnailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
