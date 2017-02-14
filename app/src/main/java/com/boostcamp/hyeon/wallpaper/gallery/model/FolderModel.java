package com.boostcamp.hyeon.wallpaper.gallery.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.boostcamp.hyeon.wallpaper.app.WallpaperApplication;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by hyeon on 2017. 2. 13..
 */

public class FolderModel {

    public void syncContentProviderToRealm(Context context){
        //read all images from Content Provider to Cursor Object.
        String[] projection = {
                MediaStore.Images.Media.BUCKET_ID, //Folder ID
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, //Folder Name
                MediaStore.Images.Media._ID, //Image ID
                MediaStore.Images.Media.ORIENTATION, //Image Orientation.
                MediaStore.Images.Media.DATE_TAKEN //Image Taken Date.
        };
        String order = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC, "+ MediaStore.Images.Media.DATE_TAKEN +" DESC";

        Cursor allImagesCursor =  context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                order
        );

        //realm object update for preparing sync
        updateRealmObjectForBeforeSync();

        if (allImagesCursor == null) {
            // error handling
        } else if (allImagesCursor.moveToFirst()) {
            //index for accessing Cursor data
            int bucketIdColumnIndex = allImagesCursor.getColumnIndex(projection[0]);
            int bucketDisplayNameColumnIndex = allImagesCursor.getColumnIndex(projection[1]);
            int idColumnIndex = allImagesCursor.getColumnIndex(projection[2]);
            int orientationColumnIndex = allImagesCursor.getColumnIndex(projection[3]);
            int dateTakenColumnIndex = allImagesCursor.getColumnIndex(projection[4]);

            do {
                //get data from Cursor
                String bucketId = allImagesCursor.getString(bucketIdColumnIndex);
                String bucketDisplayName = allImagesCursor.getString(bucketDisplayNameColumnIndex);
                int imageId = allImagesCursor.getInt(idColumnIndex);
                int orientation = allImagesCursor.getInt(orientationColumnIndex);
                String dateTaken = allImagesCursor.getString(dateTakenColumnIndex);

                //init realm
                Realm realm = WallpaperApplication.getRealmInstance();
                realm.beginTransaction();

                // if bucket isn't exist in realm, create realm object
                Folder folder = realm.where(Folder.class).equalTo("bucketId", Integer.valueOf(bucketId)).findFirst();
                if(folder == null){
                    folder = realm.createObject(Folder.class);
                    folder.setBucketId(Integer.valueOf(bucketId));
                    folder.setName(bucketDisplayName);
                    folder.setImages(new RealmList<Image>());
                    folder.setOpened(false);
                }
                folder.setSynced(true);

                // if image isn't exist in realm create realm object and adding RealmList
                Image image = realm.where(Image.class).equalTo("imageId", imageId).findFirst();
                if(image == null){
                    RealmList<Image> imageRealmList = folder.getImages();
                    image = realm.createObject(Image.class);
                    image.setImageId(imageId);
                    image.setOrientation(orientation);
                    image.setDateTaken(dateTaken);
                    imageRealmList.add(image);
                }
                image.setSelected(false);
                image.setSynced(true);

                realm.commitTransaction();
            } while (allImagesCursor.moveToNext());
        } else {
            // Cursor is empty
        }
        // close Cursor
        allImagesCursor.close();

        // realm object delete if image is not exist
        deleteRealmObjectForNotExistData();
    }

    private void deleteRealmObjectForNotExistData(){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        RealmResults<Image> imageRealmResults = realm.where(Image.class).equalTo("isSynced", false).findAll();
        imageRealmResults.deleteAllFromRealm();
        RealmResults<Folder> folderRealmResults = realm.where(Folder.class).equalTo("isSynced", false).findAll();
        folderRealmResults.deleteAllFromRealm();
        realm.commitTransaction();
    }

    private void updateRealmObjectForBeforeSync(){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        RealmResults<Folder> folderRealmResults = realm.where(Folder.class).findAll();
        for(Folder folder : folderRealmResults){
            RealmList<Image> imageRealmList = folder.getImages();
            for(Image image: imageRealmList){
                image.setSynced(false);
            }
            folder.setSynced(false);
        }
        realm.commitTransaction();
    }

    public void updateRealmObjectForDefaultMode(){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        RealmResults<Image> imageRealmResults = realm.where(Image.class).equalTo("isSelected", true).findAll();
        for(Image image : imageRealmResults){
            image.setSelected(false);
        }
        realm.commitTransaction();
    }
}
