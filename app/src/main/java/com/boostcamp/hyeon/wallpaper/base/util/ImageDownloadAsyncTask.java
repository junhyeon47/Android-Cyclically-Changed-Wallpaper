package com.boostcamp.hyeon.wallpaper.base.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by hyeon on 2017. 2. 21..
 */

public class ImageDownloadAsyncTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        try {
            //url image convert to bitmap
            InputStream is = (InputStream) new URL(params[0]).getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();

            //bitmap to file and store in device storage.
            StringBuffer path = new StringBuffer();
            path.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            path.append(File.separator);
            path.append(Define.APP_FOLDER_NAME);
            path.append(File.separator);

            File file;
            file = new File(path.toString());
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    //error
                    throw new IllegalArgumentException("failed make app directory");
                }
            }

            path.append(String.valueOf(System.currentTimeMillis()));
            path.append(Define.DOWNLOAD_IMAGE_EXT);

            FileOutputStream out = new FileOutputStream(path.toString());

            bitmap.compress(Bitmap.CompressFormat.JPEG, Define.DOWNLOAD_IMAGE_QUALITY, out);
            out.close();

            //scan MediaStore
            MediaScannerConnectionHelper.getInstance().scanFile(path.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
