package com.boostcamp.hyeon.wallpaper.base.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.ProgressBar;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Folder;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by hyeon on 2017. 2. 15..
 */

public class SyncDataHelper {

    public static void insertToRealm(Context context, Handler handler){
        //read all images from Content Provider to Cursor Object.
        String[] projection = {
                MediaStore.Images.Media.BUCKET_ID, //Folder ID
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, //Folder Name
                MediaStore.Images.Media._ID, //Image ID
                MediaStore.Images.Media.DATA, //Image path
                MediaStore.Images.Media.ORIENTATION, //Image orientation
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

        //init progress
        int totalImageCount = allImagesCursor.getCount();
        int loopCount = 0;
        Bundle bundle = new Bundle();
        if(handler != null){
            bundle.putInt(context.getString(R.string.key_max), totalImageCount);
            Message message = new Message();
            message.setData(bundle);
            handler.sendMessage(message);
        }

        if (allImagesCursor == null) {
            // error handling
        } else if (allImagesCursor.moveToFirst()) {
            //index for accessing Cursor data
            int bucketIdColumnIndex = allImagesCursor.getColumnIndex(projection[0]);
            int bucketDisplayNameColumnIndex = allImagesCursor.getColumnIndex(projection[1]);
            int idColumnIndex = allImagesCursor.getColumnIndex(projection[2]);
            int pathColumnIndex = allImagesCursor.getColumnIndex(projection[3]);
            int orientationColumnIndex = allImagesCursor.getColumnIndex(projection[4]);
            int dateTakenColumnIndex = allImagesCursor.getColumnIndex(projection[5]);

            do {
                //get data from Cursor
                String bucketId = allImagesCursor.getString(bucketIdColumnIndex);
                String bucketDisplayName = allImagesCursor.getString(bucketDisplayNameColumnIndex);
                String imageId = allImagesCursor.getString(idColumnIndex);
                String path = allImagesCursor.getString(pathColumnIndex);
                String orientation = allImagesCursor.getString(orientationColumnIndex);
                String dateTaken = allImagesCursor.getString(dateTakenColumnIndex);

                //init realm
                Realm realm = WallpaperApplication.getRealmInstance();
                realm.beginTransaction();

                // if bucket isn't exist in realm, create realm object
                Folder folder = realm.where(Folder.class).equalTo("bucketId", bucketId).findFirst();
                if(folder == null){
                    folder = realm.createObject(Folder.class);
                    folder.setBucketId(bucketId);
                    folder.setName(bucketDisplayName);
                    folder.setImages(new RealmList<Image>());
                }

                // if image isn't exist in realm create realm object and adding RealmList
                Image image = realm.where(Image.class).equalTo("imageId", imageId).findFirst();
                if(image == null){
                    RealmList<Image> imageRealmList = folder.getImages();
                    image = realm.createObject(Image.class);
                    image.setBucketId(bucketId);
                    image.setImageUri(path);
                    image.setThumbnailUri(getThumbnailUri(context, Long.valueOf(imageId)));
                    image.setImageId(imageId);
                    image.setOrientation(orientation == null ? "0" : orientation);
                    image.setDateTaken(dateTaken);
                    imageRealmList.add(image);
                }

                realm.commitTransaction();


                //update handler
                loopCount++;
                if(handler != null){
                    bundle.clear();
                    bundle.putInt(context.getString(R.string.key_progress), loopCount);
                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                }

            } while (allImagesCursor.moveToNext());
        } else {
            // Cursor is empty
        }
        // close Cursor
        allImagesCursor.close();

    }

    private static String getThumbnailUri(Context context, long imageId) {
        String[] projection = { MediaStore.Images.Thumbnails.DATA };

        Cursor thumbnailCursor = context.getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?",
                new String[]{String.valueOf(imageId)},
                null);

        if (thumbnailCursor == null) {
            return null;
        } else if (thumbnailCursor.moveToFirst()) {
            int thumbnailColumnIndex = thumbnailCursor.getColumnIndex(projection[0]);

            String thumbnailPath = thumbnailCursor.getString(thumbnailColumnIndex);
            thumbnailCursor.close();
            return Uri.fromFile(new File(thumbnailPath)).toString();
        } else {
            //if thumbnail is not exit, make thumbnail
            MediaStore.Images.Thumbnails.getThumbnail(
                    context.getContentResolver(),
                    imageId,
                    MediaStore.Images.Thumbnails.MINI_KIND,
                    null
            );
            thumbnailCursor.close();
            return getThumbnailUri(context, imageId);
        }
    }

    public static void deleteToRealm(String path){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();

        //select image realm object using mPath and delete.
        Image image = realm.where(Image.class).equalTo("imageUri", path).findFirst();
        String bucketId = image.getBucketId();
        image.deleteFromRealm();

        //if this image's folder have no image, folder delete too.
        Folder folder = realm.where(Folder.class).equalTo("bucketId", bucketId).findFirst();
        if(folder.getImages().size() == 0)
            folder.deleteFromRealm();

        realm.commitTransaction();
    }

    public static void updateToRealm(Context context, String path){

    }
}
