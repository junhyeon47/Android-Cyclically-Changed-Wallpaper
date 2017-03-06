package com.boostcamp.hyeon.wallpaper.license.list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.listener.OnItemClickListener;
import com.boostcamp.hyeon.wallpaper.base.util.Define;
import com.boostcamp.hyeon.wallpaper.license.detail.LicenseDetailActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LicenseListActivity extends AppCompatActivity implements OnItemClickListener{
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_list);

        ButterKnife.bind(this);
        init();
    }

    public void init(){
        //setup ActionBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.label_open_source_license));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mList = Arrays.asList(getResources().getStringArray(R.array.license_list));

        mRecyclerView.setAdapter(new LicenseListAdapter(mList, this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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

    @Override
    public void onItemClick(int position) {
        moveToLicenseDetailActivity(position);
    }

    @Override
    public void onItemLongClick(int position) {

    }

    public void moveToLicenseDetailActivity(int position){
        Intent intent = new Intent(this, LicenseDetailActivity.class);
        intent.putExtra("resourceId", Define.OPEN_SOURCE_LICENSE_DESCRIPTIONS[position]);
        intent.putExtra("title", mList.get(position));
        startActivity(intent);
    }
}
