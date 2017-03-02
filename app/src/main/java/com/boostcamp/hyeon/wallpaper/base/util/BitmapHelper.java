package com.boostcamp.hyeon.wallpaper.base.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapHelper {
    public static Bitmap decodeFile(String filename, int inSampleSize){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(filename, options);
    }

    public static int getBitmapWidth(String imageUri){
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageUri, options);
            return options.outWidth;
        } catch(Exception e) {
            return 0;
        }
    }

    public static int getBitmapHeight(String imageUri){
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageUri, options);
            return options.outHeight;
        } catch(Exception e) {
            return 0;
        }
    }

    public static Bitmap changeOrientation(Bitmap bitmap, int orientation){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float rotateRatio = 0f;
        switch (orientation){
            case 0:
                rotateRatio = 0;
                break;
            case 90:
                rotateRatio = 90f;
                break;
            case 180:
                rotateRatio = 180f;
                break;
            case 270:
                rotateRatio = 270f;
                break;
        }
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.preRotate(rotateRatio);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, rotateMatrix, true);
    }
}
