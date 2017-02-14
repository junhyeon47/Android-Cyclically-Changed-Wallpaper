package com.boostcamp.hyeon.wallpaper.gallery.model;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public class Image extends RealmObject{
    @Required private Integer imageId;
    @Required private Integer orientation;
    @Required private String dateTaken;
    @Required private Boolean isSelected;
    @Required private Boolean isSynced;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getOrientation() {
        return orientation;
    }

    public void setOrientation(Integer orientation) {
        this.orientation = orientation;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Boolean getSynced() {
        return isSynced;
    }

    public void setSynced(Boolean synced) {
        isSynced = synced;
    }
}
