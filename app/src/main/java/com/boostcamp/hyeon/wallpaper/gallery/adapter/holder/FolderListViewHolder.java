package com.boostcamp.hyeon.wallpaper.gallery.adapter.holder;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.ImageListAdapter;
import com.boostcamp.hyeon.wallpaper.gallery.model.Folder;
import com.boostcamp.hyeon.wallpaper.gallery.presenter.ImageListPresenter;
import com.boostcamp.hyeon.wallpaper.gallery.presenter.ImageListPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyeon on 2017. 2. 12..
 */

public class FolderListViewHolder extends RecyclerView.ViewHolder implements ImageListPresenter.View{
    @BindView(R.id.iv_folder) ImageView mFolderImageView;
    @BindView(R.id.iv_folder_open) ImageView mFolderOpenImageView;
    @BindView(R.id.tv_folder_name) TextView mFolderNameTextView;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private Context mContext;
    private OnItemClickListener mOnItemCLickListener;
    private ImageListPresenterImpl mImageListPresenter;

    public FolderListViewHolder(View itemView, Context mContext, OnItemClickListener mOnItemCLickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = mContext;
        this.mOnItemCLickListener = mOnItemCLickListener;
    }

    public void bind(final Folder folder, int position){
        mFolderNameTextView.setText(folder.getName());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemCLickListener != null) {
                    mOnItemCLickListener.onItemClick(folder.getBucketId());
                }
            }
        });

        if(folder.getOpened()){
            mFolderImageView.setVisibility(View.GONE);
            mFolderOpenImageView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }else{
            mFolderImageView.setVisibility(View.VISIBLE);
            mFolderOpenImageView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        ImageListAdapter imageListAdapter = new ImageListAdapter(
                mContext,
                folder.getImages(),
                true
        );
        mRecyclerView.setAdapter(imageListAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mImageListPresenter = new ImageListPresenterImpl();
        mImageListPresenter.attachView(this);
        mImageListPresenter.setImageListAdapterModel(imageListAdapter);
        mImageListPresenter.setImageListAdapterView(imageListAdapter);
    }
    private void setSharedPreferencesValue(boolean value){
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.pref_gallery), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(mContext.getString(R.string.pref_gallery_select_key), value);
        editor.commit();
    }

    @Override
    public void changeModeForSelect() {
        setSharedPreferencesValue(true);
    }
}