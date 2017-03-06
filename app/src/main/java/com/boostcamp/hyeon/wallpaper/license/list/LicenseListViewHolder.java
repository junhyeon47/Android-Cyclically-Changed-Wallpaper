package com.boostcamp.hyeon.wallpaper.license.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyeon on 2017. 3. 6..
 */

public class LicenseListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    @BindView(R.id.layout_item) LinearLayout mItemLinearLayout;
    @BindView(R.id.tv_license_name) TextView mLicenseNameTextView;
    private int mIndex;
    private OnItemClickListener mOnItemClickListener;

    public LicenseListViewHolder(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mOnItemClickListener = onItemClickListener;
    }

    public void bind(String licenseName, int position){
        mLicenseNameTextView.setText(licenseName);
        mIndex = position;
        mItemLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mOnItemClickListener.onItemClick(mIndex);
    }
}
