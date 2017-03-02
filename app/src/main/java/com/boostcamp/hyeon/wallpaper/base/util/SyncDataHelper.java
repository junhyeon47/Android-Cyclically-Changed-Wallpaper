package com.boostcamp.hyeon.wallpaper.base.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
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
    private static final String TAG = SyncDataHelper.class.getSimpleName();
    public static void syncDataToRealm(Context context, Handler handler, String scanPath){
        //read all images from Content Provider to Cursor Object.
        String[] projection = {
                MediaStore.Images.Media.BUCKET_ID, //Folder ID
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, //Folder Name
                MediaStore.Images.Media._ID, //Image ID
                MediaStore.Images.Media.DATA, //Image path
                MediaStore.Images.Media.ORIENTATION, //Image orientation
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATE_ADDED
        };
        String selection = null;
        String[] selectionArgs = null;
        String order = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC, "+ MediaStore.Images.Media.DATE_TAKEN +" DESC, " + MediaStore.Images.Media.DATE_ADDED +" DESC";

        if(scanPath != null){
            selection = MediaStore.Images.Media.DATA + " = ?";
            selectionArgs = new String[]{ scanPath };
        }

        Cursor cursor =  context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                order
        );

        if(scanPath == null)
            updateRealmBeforeSync();

        if (cursor == null) {
            // error handling
        } else if (cursor.moveToFirst()) {
            int totalImageCount = cursor.getCount();
            int loopCount = 0;
            if(handler != null){
                Bundle bundle = new Bundle();
                bundle.putInt(context.getString(R.string.key_max), totalImageCount);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }
            //index for accessing Cursor data
            int bucketIdColumnIndex = cursor.getColumnIndex(projection[0]);
            int bucketDisplayNameColumnIndex = cursor.getColumnIndex(projection[1]);
            int idColumnIndex = cursor.getColumnIndex(projection[2]);
            int pathColumnIndex = cursor.getColumnIndex(projection[3]);
            int orientationColumnIndex = cursor.getColumnIndex(projection[4]);
            int dateTakenColumnIndex = cursor.getColumnIndex(projection[5]);
            int dateAddedColumnIndex = cursor.getColumnIndex(projection[6]);

            do {
                //get data from Cursor
                String bucketId = cursor.getString(bucketIdColumnIndex);
                String bucketDisplayName = cursor.getString(bucketDisplayNameColumnIndex);
                String imageId = cursor.getString(idColumnIndex);
                String path = cursor.getString(pathColumnIndex);
                String orientation = cursor.getString(orientationColumnIndex);
                String dateTaken = cursor.getString(dateTakenColumnIndex);
                String dateAdded = cursor.getString(dateAddedColumnIndex);

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
                    folder.setOpened(false);
                }
                if(scanPath == null){
                    folder.setOpened(false);
                }
                folder.setSynced(true);

                // if image isn't exist in realm create realm object and adding RealmList
                Image image = realm.where(Image.class).equalTo("imageId", imageId).findFirst();
                if(image == null){
                    RealmList<Image> imageRealmList = folder.getImages();
                    image = realm.createObject(Image.class);
                    image.setBucketId(bucketId);
                    //image.setImageUri(Uri.fromFile(new File(path)).toString());
                    image.setImageUri(path);
                    image.setThumbnailUri(getThumbnailUri(context, Long.valueOf(imageId)));
                    image.setImageId(imageId);
                    image.setOrientation(orientation == null ? "0" : orientation);
                    image.setDateTaken(dateTaken);
                    image.setDateAdded(dateAdded);
                    imageRealmList.add(image);
                }
                image.setSelected(false);
                image.setNumber(null);
                image.setSynced(true);

                realm.commitTransaction();


                //update handler
                if(handler != null){
                    loopCount++;
                    Bundle bundle = new Bundle();
                    bundle.clear();
                    bundle.putInt(context.getString(R.string.key_progress), loopCount);
                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                }

            } while (cursor.moveToNext());
            // close Cursor
            cursor.close();
        } else {
            // Cursor is empty
        }
        if(scanPath == null)
            deleteRealmAfterSync();
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
            //return Uri.fromFile(new File(thumbnailPath)).toString();
            return thumbnailPath;
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

    private static void updateRealmBeforeSync(){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();

        RealmResults<Folder> folderRealmResults = realm.where(Folder.class).findAll();

        for(Folder folder : folderRealmResults){
            folder.setSynced(false);
            for(Image image : folder.getImages())
                image.setSynced(false);
        }

        realm.commitTransaction();
    }

    private static void deleteRealmAfterSync(){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();

        RealmResults<Folder> folderRealmResults = realm.where(Folder.class).equalTo("isSynced", false).findAll();
        for(Folder folder : folderRealmResults){
            folder.deleteFromRealm();
        }

        RealmResults<Image> imageRealmResults = realm.where(Image.class).equalTo("isSynced", false).findAll();
        for(Image image : imageRealmResults){
            image.deleteFromRealm();
        }
        realm.commitTransaction();
    }

}
