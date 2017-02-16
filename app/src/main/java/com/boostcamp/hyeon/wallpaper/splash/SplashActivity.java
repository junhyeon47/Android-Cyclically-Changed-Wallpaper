package com.boostcamp.hyeon.wallpaper.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;
import com.boostcamp.hyeon.wallpaper.base.util.SyncDataHelper;
import com.boostcamp.hyeon.wallpaper.main.view.MainActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, PermissionListener, Handler.Callback{
    private static final int FIRST_SYNC_DATA_LOADER = 1;
    private static final int DELAY_MILLIS = 1000;
    private Handler mHandler;
    @BindView(R.id.progress_bar) RoundCornerProgressBar mRoundCornerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        //init Handler
        mHandler = new Handler(this);

        //check Permission
        new TedPermission(this)
                .setPermissionListener(this)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(this){
            @Override
            public Void loadInBackground() {
                SyncDataHelper.syncContentProviderToRealm(getApplicationContext(), mHandler);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.BOOLEAN_FIRST_INIT, false);
        moveToMainActivity();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    private void moveToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPermissionGranted() {
        //Toast.makeText(SplashActivity.this, "Permission Granted ", Toast.LENGTH_SHORT).show();
        if(SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_FIRST_INIT, true)) {
            mRoundCornerProgressBar.setVisibility(View.VISIBLE);
            getSupportLoaderManager().initLoader(FIRST_SYNC_DATA_LOADER, null, this).forceLoad();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveToMainActivity();
                }
            }, DELAY_MILLIS);
        }
    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        Toast.makeText(SplashActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        if(bundle != null) {
            Object max = bundle.get(getString(R.string.key_max));
            Object progress = bundle.get(getString(R.string.key_progress));
            if(max != null){
                mRoundCornerProgressBar.setMax((int)max);
                mRoundCornerProgressBar.setSecondaryProgress((int)max);
            }
            if(progress != null){
                mRoundCornerProgressBar.setProgress((int)progress);

            }
        }
        return true;
    }
}
