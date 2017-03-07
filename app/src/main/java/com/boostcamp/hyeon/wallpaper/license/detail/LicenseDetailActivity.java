package com.boostcamp.hyeon.wallpaper.license.detail;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LicenseDetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tv_license_description) TextView mLicenseDescriptionTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_detail);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        int resourceId = getIntent().getIntExtra("resourceId", 0);
        String title = getIntent().getStringExtra("title");

        new AsyncTask<Integer, Void, String>(){
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Integer... params) {
                return loadTextFile(params[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                mLicenseDescriptionTextView.setText(s);
            }
        }.execute(resourceId);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private String loadTextFile(int resourceId){
        StringBuilder result = new StringBuilder();
        try{
            InputStream in = getResources().openRawResource(resourceId);
            if(in != null){
                InputStreamReader reader = new InputStreamReader(in, "utf-8");
                BufferedReader buff = new BufferedReader(reader);

                String read;

                while((read = buff.readLine()) != null) {
                    result.append(read);
                    result.append(System.getProperty("line.separator"));
                }
                in.close();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
