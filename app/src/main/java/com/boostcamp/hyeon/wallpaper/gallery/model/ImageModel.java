package com.boostcamp.hyeon.wallpaper.gallery.model;

import com.boostcamp.hyeon.wallpaper.app.WallpaperApplication;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public class ImageModel {
    private void updateRealmObjectForBeforeSelecting(){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        RealmResults<Image> imageRealmResults = realm.where(Image.class).equalTo("isSelected", true).findAll();
        for(Image image: imageRealmResults){
            image.setSelected(false);
        }
        realm.commitTransaction();
    }
}
