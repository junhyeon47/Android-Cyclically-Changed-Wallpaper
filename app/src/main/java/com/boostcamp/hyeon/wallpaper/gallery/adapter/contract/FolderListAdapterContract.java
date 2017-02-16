package com.boostcamp.hyeon.wallpaper.gallery.adapter.contract;

import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public interface FolderListAdapterContract {
    interface View {
        void notifyAdapter();
        void notifyAdapter(int position);
        void setOnItemClickListener(OnItemClickListener listener);
    }

    interface Model {
        void update(int position);
    }
}
