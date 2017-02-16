package com.boostcamp.hyeon.wallpaper.gallery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.hyeon.wallpaper.R;

import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Folder;
import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.contract.FolderListAdapterContract;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.holder.FolderListViewHolder;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by hyeon on 2017. 2. 12..
 */

public class FolderListAdapter extends RealmRecyclerViewAdapter<Folder, FolderListViewHolder> implements FolderListAdapterContract.Model, FolderListAdapterContract.View{
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

    @Override
    public void update(int position) {
        Realm realm = WallpaperApplication.getRealmInstance();
        WallpaperApplication.getRealmInstance().beginTransaction();
        RealmResults<Folder> folderRealmResults = realm.where(Folder.class).findAll();
        for(Folder folder : folderRealmResults){
            if(folder.getBucketId().equals(getData().get(position).getBucketId())){
                //select item
                folder.setOpened(true);
            }else{
                folder.setOpened(false);
            }
        }
        realm.commitTransaction();
    }

}
