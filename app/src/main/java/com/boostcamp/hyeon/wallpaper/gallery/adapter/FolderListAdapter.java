package com.boostcamp.hyeon.wallpaper.gallery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.hyeon.wallpaper.R;

import com.boostcamp.hyeon.wallpaper.base.adapter.RealmListAdapterContract;
import com.boostcamp.hyeon.wallpaper.base.domain.Folder;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.holder.FolderListViewHolder;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by hyeon on 2017. 2. 12..
 */

public class FolderListAdapter extends RealmRecyclerViewAdapter<Folder, FolderListViewHolder> implements RealmListAdapterContract.Model<Folder>, RealmListAdapterContract.View{
    private OnItemClickListener mOnItemClickListener;

    public FolderListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Folder> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    @Override
    public FolderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.item_folder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForItem, parent, false);
        return new FolderListViewHolder(view, context, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(FolderListViewHolder holder, int position) {
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
