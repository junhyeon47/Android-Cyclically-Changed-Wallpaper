package com.boostcamp.hyeon.wallpaper.license.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by hyeon on 2017. 3. 6..
 */

public class LicenseListAdapter extends RecyclerView.Adapter<LicenseListViewHolder> {
    private List<String> mList;
    private OnItemClickListener mOnItemClickListener;

    public LicenseListAdapter(List<String> mList, OnItemClickListener mOnItemClickListener) {
        this.mList = mList;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public LicenseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.item_license_list;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForItem, parent, false);
        return new LicenseListViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(LicenseListViewHolder holder, int position) {
        holder.bind(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
