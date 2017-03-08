package com.boostcamp.hyeon.wallpaper.gallery.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.base.util.DisplayMetricsHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyeon on 2017. 2. 12..
 */

public class ImageListViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ImageListViewHolder.class.getSimpleName();
    @BindView(R.id.layout_item) FrameLayout mItemFrameLayout;
    @BindView(R.id.iv_thumbnail) ImageView mThumbnailImageView;
    @BindView(R.id.layout_border) FrameLayout mBorderFrameLayout;
    @BindView(R.id.layout_number) LinearLayout mNumberLinearLayout;
    @BindView(R.id.tv_number) TextView mNumberTextView;
    private Context mContext;
    private OnItemClickListener mOnItemCLickListener;

    public ImageListViewHolder(View itemView, Context mContext, OnItemClickListener mOnItemCLickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = mContext;
        this.mOnItemCLickListener = mOnItemCLickListener;

        int size = DisplayMetricsHelper.getInstance().getDeviceWidth()*2/9;

        ViewGroup.LayoutParams layoutParams = mItemFrameLayout.getLayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
        mItemFrameLayout.setLayoutParams(layoutParams);

        layoutParams = mThumbnailImageView.getLayoutParams();
        layoutParams.width = size-1;
        layoutParams.height = size-1;
        mThumbnailImageView.setLayoutParams(layoutParams);

        layoutParams = mBorderFrameLayout.getLayoutParams();
        layoutParams.width = size-1;
        layoutParams.height = size-1;
        mBorderFrameLayout.setLayoutParams(layoutParams);
    }

    public void bind(Image image, final int position){
        Glide.with(mContext)
                .load(image.getImageUri())
                .centerCrop()
                .into(mThumbnailImageView);

        if(image.getSelected()){
            mBorderFrameLayout.setVisibility(View.VISIBLE);
            mNumberLinearLayout.setVisibility(View.VISIBLE);
            mNumberTextView.setText(String.valueOf(image.getNumber()));
        }else{
            mBorderFrameLayout.setVisibility(View.GONE);
            mNumberLinearLayout.setVisibility(View.GONE);
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
    }
}
