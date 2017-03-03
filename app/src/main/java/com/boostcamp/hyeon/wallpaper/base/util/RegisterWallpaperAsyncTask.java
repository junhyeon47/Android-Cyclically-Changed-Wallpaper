package com.boostcamp.hyeon.wallpaper.base.util;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.domain.Wallpaper;
import com.boostcamp.hyeon.wallpaper.gallery.view.GalleryFragment;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by hyeon on 2017. 3. 1..
 */

public class RegisterWallpaperAsyncTask extends AsyncTask<Integer, Void, Void> {
    private Fragment mFragment;
    private AlertDialog mAlertDialog;

    public RegisterWallpaperAsyncTask(Fragment fragment) {
        this.mFragment = fragment;
        mAlertDialog = new SpotsDialog(fragment.getActivity(), R.style.CustomWallpaperSpotsDialog);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((GalleryFragment)mFragment).showRegisterDialog();
        mAlertDialog.show();
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int changeCycle = params[0];
        int changeScreenType = params[1];

        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();

        Wallpaper wallpaper = realm.where(Wallpaper.class).findFirst();
        if(wallpaper == null)
            wallpaper = realm.createObject(Wallpaper.class);
        else
            wallpaper.setImages(null);

        RealmResults<Image> imageRealmResults = realm.where(Image.class).equalTo("isSelected", true).findAllSorted("number", Sort.ASCENDING);
        RealmList<Image> imageRealmList = new RealmList<>();
        imageRealmList.addAll(imageRealmResults.subList(0, imageRealmResults.size()));
        wallpaper.setImages(imageRealmList);
        wallpaper.setCurrentPosition(0);
        wallpaper.setNextPosition(0);
        wallpaper.setChangeCycle(changeCycle);
        wallpaper.setChangeScreenType(changeScreenType);
        wallpaper.setUsing(Define.USE_WALLPAPER);
        wallpaper.setRandom(false);
        wallpaper.setTransparent(false);
        realm.commitTransaction();

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(System.currentTimeMillis()+Define.DELAY_MILLIS);
        AlarmManagerHelper.unregisterToAlarmManager(mFragment.getActivity(), Define.ID_ALARM_DEFAULT);
        AlarmManagerHelper.registerToAlarmManager(mFragment.getActivity(), date, Define.ID_ALARM_DEFAULT);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ((GalleryFragment)mFragment).changeModeForDefault();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((GalleryFragment)mFragment).hideRegisterDialog();
                mAlertDialog.dismiss();
            }
        }, 1000);
        super.onPostExecute(aVoid);
    }
}
