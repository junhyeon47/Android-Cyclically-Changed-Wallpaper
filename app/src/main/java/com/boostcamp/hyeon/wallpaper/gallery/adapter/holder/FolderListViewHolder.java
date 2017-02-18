package com.boostcamp.hyeon.wallpaper.gallery.adapter.holder;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Folder;
import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyeon on 2017. 2. 12..
 */

public class FolderListViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.iv_folder_icon) ImageView mFolderIconImageView;
    @BindView(R.id.iv_folder_open_icon) ImageView mFolderOpenIconImageView;
    @BindView(R.id.tv_folder_name) TextView mFolderNameTextView;
    @BindView(R.id.iv_folder_image) ImageView mFolderImageView;
    private Context mContext;
    private OnItemClickListener mOnItemCLickListener;
    private int mSize;

    public FolderListViewHolder(View itemView, Context mContext, OnItemClickListener mOnItemCLickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = mContext;
        this.mOnItemCLickListener = mOnItemCLickListener;

        this.mSize = ((WallpaperApplication)mContext.getApplicationContext()).mDeviceWidthSize/4;
    }

    public void bind(final Folder folder, final int position){
        mFolderNameTextView.setText(folder.getName());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemCLickListener != null) {
                    mOnItemCLickListener.onItemClick(position);
                }
            }
        });

        Picasso.with(mContext)
                .load(Uri.parse(folder.getImages().get(0).getThumbnailUri()))
                .rotate(Integer.valueOf(folder.getImages().get(0).getOrientation()))
                .resize(mSize, mSize)
                .centerCrop()
                .into(mFolderImageView);
    }
}