package com.boostcamp.hyeon.wallpaper.base.domain;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by hyeon on 2017. 2. 15..
 */

public class Folder extends RealmObject{
    private String bucketId;
    private String name;
    private RealmList<Image> images;

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Image> getImages() {
        return images;
    }

    public void setImages(RealmList<Image> images) {
        this.images = images;
    }
}
