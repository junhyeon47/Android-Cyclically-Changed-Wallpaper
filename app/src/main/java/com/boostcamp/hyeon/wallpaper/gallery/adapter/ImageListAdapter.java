package com.boostcamp.hyeon.wallpaper.gallery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.hyeon.wallpaper.R;

import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.contract.ImageListAdapterContract;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.holder.ImageListViewHolder;


import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by hyeon on 2017. 2. 12..
 */

public class ImageListAdapter extends RealmRecyclerViewAdapter<Image, ImageListViewHolder> implements ImageListAdapterContract.Model, ImageListAdapterContract.View {
    private OnItemClickListener mOnItemClickListener;

    public ImageListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Image> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    @Override
    public ImageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.item_image;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForItem, parent, false);
        return new ImageListViewHolder(view, context, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(ImageListViewHolder holder, int position) {
        holder.bind(getData().get(position), position);
    }

    @Override
    public void update(int position) {
        WallpaperApplication.getRealmInstance().beginTransaction();
        getData().get(position).setSelected(!getData().get(position).getSelected());
        WallpaperApplication.getRealmInstance().commitTransaction();
    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public void notifyAdapter(int position) {
        notifyItemChanged(position);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
