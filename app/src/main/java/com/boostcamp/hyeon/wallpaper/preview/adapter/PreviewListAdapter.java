package com.boostcamp.hyeon.wallpaper.preview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.adapter.RealmListAdapterContract;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.preview.adapter.holder.PreviewListViewHolder;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by hyeon on 2017. 2. 20..
 */

public class PreviewListAdapter extends RealmRecyclerViewAdapter<Image, PreviewListViewHolder> implements RealmListAdapterContract.Model<Image>, RealmListAdapterContract.View {
    private OnItemClickListener mOnItemClickListener;

    public PreviewListAdapter(@Nullable OrderedRealmCollection<Image> data, boolean autoUpdate) {
        super(data, autoUpdate);
    }

    @Override
    public PreviewListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.item_preview;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForItem, parent, false);
        return new PreviewListViewHolder(view, context, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(PreviewListViewHolder holder, int position) {
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
