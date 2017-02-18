package com.boostcamp.hyeon.wallpaper.base.util;

import android.content.Context;
import android.os.FileObserver;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Folder;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;

import io.realm.Realm;

/**
 * Created by hyeon on 2017. 2. 17..
 */

public class ImageFileObserver extends FileObserver {
    private static final String TAG = ImageFileObserver.class.getSimpleName();
    private Context mContext;
    private String mPath;

    public ImageFileObserver(String path, Context mContext) {
        super(path);
        this.mPath = path;
        this.mContext = mContext;
    }
    @Override
    public void onEvent(int event, String path) {
        if(path != null)
            mPath += path;
        switch(event){
            case FileObserver.DELETE:
            case FileObserver.DELETE_SELF:
                Log.d(TAG, "DELETE OR DELETE_SELF: " + mPath);
                SyncDataHelper.deleteToRealm(mPath);
                break;
            case FileObserver.MODIFY:
            case FileObserver.MOVED_TO:
            case FileObserver.MOVE_SELF:
                Log.d(TAG, "MODIFY MOVED_TO MOVE_SELF: " + mPath);
                //realm database update
                //get Image's cursor info using content provider
                //select image realm object using mPath and update.
                break;
        }
    }
}
