package com.boostcamp.hyeon.wallpaper.base.domain;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by hyeon on 2017. 2. 20..
 */

public class Wallpaper extends RealmObject{
    private Integer currentPosition;
    private RealmList<Image> images;

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }

    public RealmList<Image> getImages() {
        return images;
    }

    public void setImages(RealmList<Image> images) {
        this.images = images;
    }
}
