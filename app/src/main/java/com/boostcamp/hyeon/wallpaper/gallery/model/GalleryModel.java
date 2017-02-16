package com.boostcamp.hyeon.wallpaper.gallery.model;

import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by hyeon on 2017. 2. 16..
 */

public class GalleryModel {

    public void updateAllImagesDeselected(){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();

        RealmResults<Image> imageRealmResults = realm.where(Image.class).equalTo("isSelected", true).findAll();

        for(Image image : imageRealmResults){
            image.setSelected(false);
        }

        realm.commitTransaction();
    }
}
