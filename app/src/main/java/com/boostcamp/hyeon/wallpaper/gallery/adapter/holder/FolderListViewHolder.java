package com.boostcamp.hyeon.wallpaper.gallery.adapter.holder;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Folder;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.base.util.DisplayMetricsHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by hyeon on 2017. 2. 12..
 */

public class FolderListViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.layout_item) LinearLayout mItemLinearLayout;
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

        this.mSize = DisplayMetricsHelper.getInstance().getDeviceWidth()/3;

        ViewGroup.LayoutParams layoutParams = mItemLinearLayout.getLayoutParams();
        layoutParams.width = mSize;
        mItemLinearLayout.setLayoutParams(layoutParams);

        int padding = (int) (20 * Resources.getSystem().getDisplayMetrics().density);

        layoutParams = mFolderImageView.getLayoutParams();
        layoutParams.width = mSize - padding;
        layoutParams.height = mSize - padding;
        mFolderImageView.setLayoutParams(layoutParams);
    }

    public void bind(Folder folder, final int position){
        mFolderNameTextView.setText(folder.getName());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemCLickListener != null) {
                    mOnItemCLickListener.onItemClick(position);
                }
            }
        });

        if(folder.getOpened()){
            mFolderIconImageView.setVisibility(View.GONE);
            mFolderOpenIconImageView.setVisibility(View.VISIBLE);
        }else{
            mFolderIconImageView.setVisibility(View.VISIBLE);
            mFolderOpenIconImageView.setVisibility(View.GONE);
        }

        Image image = folder.getImages().sort("dateAdded", Sort.DESCENDING).first();

        Picasso.with(mContext)
                .load(new File(image.getThumbnailUri()))
                .rotate(Integer.valueOf(image.getOrientation()))
                .fit()
                .centerCrop()
                .into(mFolderImageView);
    }
}