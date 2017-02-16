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

    public String getOpenedFolderId(){
        Realm realm = WallpaperApplication.getRealmInstance();
        RealmResults<Folder> folderRealmResults = realm.where(Folder.class).findAll();
        String openedFolderId = null;
        for(Folder folder : folderRealmResults){
            if(folder.getOpened()){
                openedFolderId = folder.getBucketId();
                break;
            }
        }
        if(openedFolderId == null){
            folderRealmResults.get(0).setOpened(true);
            openedFolderId = folderRealmResults.get(0).getBucketId();
        }
        return openedFolderId;
    }

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
