package com.boostcamp.hyeon.wallpaper.base.util;

import android.content.Context;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

/**
 * Created by hyeon on 2017. 2. 21..
 */

public class MediaScannerConnectionHelper {
    private static final String TAG = MediaScannerConnectionHelper.class.getSimpleName();
    private MediaScannerConnection mMediaScannerConnection;
    private MediaScannerConnection.MediaScannerConnectionClient mMediaScannerConnectionClient;
    private String mPath;

    private MediaScannerConnectionHelper() {
    }

    private static class MediaScannerConnectionHelperHolder{
        public static final MediaScannerConnectionHelper INSTANCE = new MediaScannerConnectionHelper();

    }

    public static MediaScannerConnectionHelper getInstance(final Context context){
        final MediaScannerConnectionHelper instance = MediaScannerConnectionHelperHolder.INSTANCE;
        if(instance.mMediaScannerConnection == null){
            instance.mMediaScannerConnectionClient = new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {
                    Log.d(TAG, "onMediaScannerConnected()");
                    instance.mMediaScannerConnection.scanFile(instance.mPath, null);
                }

                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Log.d(TAG, "onScanCompleted");
                    Log.d(TAG, "path: "+ path);
                    Log.d(TAG, "uri: "+uri);
                    SyncDataHelper.syncDataToRealm(context, null, path);
                    instance.mMediaScannerConnection.disconnect();
                }
            };
        }
        instance.mMediaScannerConnection = new MediaScannerConnection(context, instance.mMediaScannerConnectionClient);
        return instance;
    }

    public static MediaScannerConnectionHelper getInstance(){
        MediaScannerConnectionHelper instance = MediaScannerConnectionHelperHolder.INSTANCE;
        if(instance.mMediaScannerConnection != null && instance.mMediaScannerConnectionClient != null){
            return MediaScannerConnectionHelperHolder.INSTANCE;
        }
        throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");
    }

    public void scanFile(String path){
        mPath = path;
        mMediaScannerConnection.connect();
    }
}
