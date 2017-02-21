package com.boostcamp.hyeon.wallpaper.base.domain;

import io.realm.RealmObject;

/**
 * Created by hyeon on 2017. 2. 21..
 */


public class ImageNaver extends RealmObject{
    private String link;
    private String thumbnail;
    private String sizeWidth;
    private String sizeHeight;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSizeWidth() {
        return sizeWidth;
    }

    public void setSizeWidth(String sizeWidth) {
        this.sizeWidth = sizeWidth;
    }

    public String getSizeHeight() {
        return sizeHeight;
    }

    public void setSizeHeight(String sizeHeight) {
        this.sizeHeight = sizeHeight;
    }

}