package com.boostcamp.hyeon.wallpaper.search.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.domain.ImageNaver;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.base.util.DisplayMetricsHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyeon on 2017. 2. 21..
 */

public class ImageNaverListViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.layout_item) FrameLayout mItemFrameLayout;
    @BindView(R.id.iv_thumbnail) ImageView mThumbnailImageView;
    @BindView(R.id.tv_number) TextView mNumberTextView;
    @BindView(R.id.tv_error_message)TextView mErrorMessageTextView;

    private Context mContext;
    private OnItemClickListener mOnItemCLickListener;

    public ImageNaverListViewHolder(View itemView, Context mContext, OnItemClickListener mOnItemCLickListener) {
        super(itemView);
        this.mContext = mContext;
        this.mOnItemCLickListener = mOnItemCLickListener;
        ButterKnife.bind(this, itemView);

        int width = DisplayMetricsHelper.getInstance().getDeviceWidth()/4;
        int height = DisplayMetricsHelper.getInstance().getDeviceHeight()/4;

        ViewGroup.LayoutParams layoutParams = mItemFrameLayout.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mItemFrameLayout.setLayoutParams(layoutParams);

        layoutParams = mThumbnailImageView.getLayoutParams();
        layoutParams.width = width-1;
        layoutParams.height = height-1;
        mThumbnailImageView.setLayoutParams(layoutParams);

    }

    public void bind(final ImageNaver imageNaver, final int position){
        mErrorMessageTextView.setVisibility(View.GONE);
        Picasso.with(mContext)
                .load(imageNaver.getThumbnail())
                .fit()
                .centerCrop()
                .into(mThumbnailImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        mErrorMessageTextView.setVisibility(View.VISIBLE);
                    }
                });

        mNumberTextView.setVisibility(View.GONE);

        mThumbnailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemCLickListener.onItemClick(position);
            }
        });
    }
}
