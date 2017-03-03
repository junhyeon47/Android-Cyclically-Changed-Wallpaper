package com.boostcamp.hyeon.wallpaper.base.adapter;

import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;

/**
 * Created by hyeon on 2017. 2. 15..
 */

public interface RealmListAdapterContract {
    interface View {
        void notifyAdapter();
        void notifyAdapter(int position);
        void setOnItemClickListener(OnItemClickListener listener);
    }

    interface Model<T> {
        T getItem(int position);
    }
}
