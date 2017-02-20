package com.boostcamp.hyeon.wallpaper.gallery.model;

import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Folder;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by hyeon on 2017. 2. 16..
 */

public class GalleryModel {

    public void selectFolder(String bucketId){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        RealmResults<Folder> folderRealmResults = realm.where(Folder.class).findAll();
        for(Folder folder : folderRealmResults){
            if(bucketId.equals(folder.getBucketId()))
                folder.setOpened(true);
            else
                folder.setOpened(false);
        }
        realm.commitTransaction();
    }

    public void selectImage(String imageId){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        Image image = realm.where(Image.class).equalTo("imageId", imageId).findFirst();
        int totalSelectedNumber = realm.where(Image.class).equalTo("isSelected", true).findAll().size();
        if(!image.getSelected()){
            image.setNumber(totalSelectedNumber+1);
            image.setSelected(true);
        }else {
            RealmResults<Image> imageRealmResults = realm.where(Image.class).greaterThan("number", image.getNumber()).findAll();
            for(Image greaterImage : imageRealmResults){
                greaterImage.setNumber(greaterImage.getNumber()-1);
            }
            image.setSelected(false);
        }
        realm.commitTransaction();
    }

    public void updateAllImagesDeselected(){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();

        RealmResults<Image> imageRealmResults = realm.where(Image.class).equalTo("isSelected", true).findAll();

        for(Image image : imageRealmResults){
            image.setSelected(false);
            image.setNumber(null);
        }
        realm.commitTransaction();
    }
}
