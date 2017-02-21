package com.boostcamp.hyeon.wallpaper.search.adapter.holder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.ImageNaver;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyeon on 2017. 2. 21..
 */

public class ImageNaverViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.layout_item) FrameLayout mItemFrameLayout;
    @BindView(R.id.iv_thumbnail) ImageView mThumbnailImageView;
    @BindView(R.id.iv_select) ImageView mSelectImageView;
    @BindView(R.id.layout_select) RelativeLayout mSelectRelativeLayout;
    @BindView(R.id.tv_number) TextView mNumberTextView;

    private Context mContext;
    private OnItemClickListener mOnItemCLickListener;

    public ImageNaverViewHolder(View itemView, Context mContext, OnItemClickListener mOnItemCLickListener) {
        super(itemView);
        this.mContext = mContext;
        this.mOnItemCLickListener = mOnItemCLickListener;
        ButterKnife.bind(this, itemView);

        int width = ((WallpaperApplication)mContext.getApplicationContext()).mDeviceWidthSize/4;
        int height = ((WallpaperApplication)mContext.getApplicationContext()).mDeviceHeightSize/4;

        ViewGroup.LayoutParams layoutParams = mItemFrameLayout.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mItemFrameLayout.setLayoutParams(layoutParams);
    }

    public void bind(ImageNaver imageNaver, final int position){
        Picasso.with(mContext)
                .load(Uri.parse(imageNaver.getThumbnail()))
                .fit()
                .centerCrop()
                .into(mThumbnailImageView);

        mNumberTextView.setVisibility(View.GONE);

        mThumbnailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemCLickListener.onItemClick(position);
            }
        });
    }
}
