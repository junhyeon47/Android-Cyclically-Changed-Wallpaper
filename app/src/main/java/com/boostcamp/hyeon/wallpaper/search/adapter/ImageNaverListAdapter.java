package com.boostcamp.hyeon.wallpaper.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.adapter.RealmListAdapterContract;
import com.boostcamp.hyeon.wallpaper.base.domain.ImageNaver;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.search.adapter.holder.ImageNaverViewHolder;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by hyeon on 2017. 2. 21..
 */

public class ImageNaverListAdapter extends RealmRecyclerViewAdapter<ImageNaver, ImageNaverViewHolder> implements RealmListAdapterContract.Model<ImageNaver>, RealmListAdapterContract.View{
    private OnItemClickListener mOnItemClickListener;

    public ImageNaverListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<ImageNaver> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    @Override
    public ImageNaverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.item_image;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForItem, parent, false);
        return new ImageNaverViewHolder(view, context, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(ImageNaverViewHolder holder, int position) {
        holder.bind(getData().get(position), position);
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
