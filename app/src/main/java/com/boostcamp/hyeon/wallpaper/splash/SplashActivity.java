package com.boostcamp.hyeon.wallpaper.splash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.util.SyncDataHelper;
import com.boostcamp.hyeon.wallpaper.main.view.MainActivity;


import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, Handler.Callback{
    private static final int PERMISSION_READ = 23;
    private static final int FIRST_SYNC_DATA_LOADER = 1;
    private static final int DELAY_MILLIS = 2000;
    private Handler mHandler;
    private long mStartTime;
    private long mEndTime;
    @BindView(R.id.progress_bar) RoundCornerProgressBar mRoundCornerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        //init Handler
        mHandler = new Handler(this);

        //check Permission
        checkPermission();
    }

    public void init(){
        getSupportLoaderManager().initLoader(FIRST_SYNC_DATA_LOADER, null, this);
    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(this){
            @Override
            protected void onStartLoading() {
                mStartTime = System.currentTimeMillis();
                forceLoad();
            }

            @Override
            public Void loadInBackground() {
                SyncDataHelper.syncDataToRealm(getApplicationContext(), mHandler);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        mEndTime = System.currentTimeMillis();
        moveToMainActivity();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    private void moveToMainActivity(){
        long loadingTime = mEndTime - mStartTime;
        final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        if(loadingTime > DELAY_MILLIS){
            startActivity(intent);
            finish();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    finish();
                }
            }, loadingTime);
        }
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

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, PERMISSION_READ);
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_READ:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                }else{
                    finish();
                }
                break;
        }
    }
}
