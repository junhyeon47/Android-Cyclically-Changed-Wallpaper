package com.boostcamp.hyeon.wallpaper.base.domain;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by hyeon on 2017. 2. 20..
 */

public class Wallpaper extends RealmObject{
    private int currentPosition;
    private int nextPosition;
    private int changeCycle;
    private int changeScreenType;
    private boolean isUsing;
    private boolean isRandom;
    private boolean isTransparent;
    private RealmList<Image> images;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }

    public int getChangeCycle() {
        return changeCycle;
    }

    public void setChangeCycle(int changeCycle) {
        this.changeCycle = changeCycle;
    }

    public int getChangeScreenType() {
        return changeScreenType;
    }

    public void setChangeScreenType(int changeScreenType) {
        this.changeScreenType = changeScreenType;
    }

    public boolean isUsing() {
        return isUsing;
    }

    public void setUsing(boolean using) {
        isUsing = using;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public void setRandom(boolean random) {
        isRandom = random;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setTransparent(boolean transparent) {
        isTransparent = transparent;
    }

    public RealmList<Image> getImages() {
        return images;
    }

    public void setImages(RealmList<Image> images) {
        this.images = images;
    }
}
