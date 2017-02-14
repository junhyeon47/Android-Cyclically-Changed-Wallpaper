package com.boostcamp.hyeon.wallpaper.gallery.adapter.contract;

import com.boostcamp.hyeon.wallpaper.listener.OnItemClickListener;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public interface ImageListAdapterContract {
    interface View {
        void notifyAdapter();
        void setOnItemClickListener(OnItemClickListener listener);
    }

    interface Model {
        void update(int imageId);
    }
}