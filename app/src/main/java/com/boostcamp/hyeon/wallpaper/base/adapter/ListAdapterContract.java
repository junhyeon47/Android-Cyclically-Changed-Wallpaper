package com.boostcamp.hyeon.wallpaper.base.adapter;

import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by hyeon on 2017. 2. 15..
 */

public interface ListAdapterContract {
    interface View {
        void notifyAdapter();
        void setOnItemClickListener(OnItemClickListener listener);
    }

    interface Model<T> {
        void setListData(List<T> listItem);
        void addListData(T item);
    }
}
