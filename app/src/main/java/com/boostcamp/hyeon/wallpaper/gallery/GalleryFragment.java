package com.boostcamp.hyeon.wallpaper.gallery;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.FolderListAdapter;
import com.boostcamp.hyeon.wallpaper.gallery.model.Folder;
import com.boostcamp.hyeon.wallpaper.gallery.presenter.FolderListPresenter;
import com.boostcamp.hyeon.wallpaper.gallery.presenter.FolderListPresenterImpl;
import com.boostcamp.hyeon.wallpaper.listener.OnBackKeyPressedListener;
import com.boostcamp.hyeon.wallpaper.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.Sort;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment implements FolderListPresenter.View, SharedPreferences.OnSharedPreferenceChangeListener, OnBackKeyPressedListener{
    private static final String TAG = GalleryFragment.class.getSimpleName();
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private FolderListPresenterImpl mFolderListPresenter;
    private SharedPreferences mSharedPreferences;
    private MenuItem mSelectMenuItem, mPreviewMenuItem, mDoneMenuItem;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for use option menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);

        init();
        return view;
    }

    public void init(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        Realm realm = WallpaperApplication.getRealmInstance();
        FolderListAdapter folderListAdapter = new FolderListAdapter(
                getContext(),
                realm.where(Folder.class).findAllSortedAsync("name", Sort.ASCENDING),
                true
        );
        mRecyclerView.setAdapter(folderListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mFolderListPresenter = new FolderListPresenterImpl();
        mFolderListPresenter.attachView(this);
        mFolderListPresenter.setFolderListAdapterModel(folderListAdapter);
        mFolderListPresenter.setFolderListAdapterView(folderListAdapter);
        mFolderListPresenter.loadFolderList(getContext());

        //init SharedPreferences
        mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.pref_gallery), MODE_PRIVATE);
        setSharedPreferencesValue(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_gallery, menu);
        mSelectMenuItem = menu.findItem(R.id.menu_select);
        mPreviewMenuItem = menu.findItem(R.id.menu_preview);
        mDoneMenuItem = menu.findItem(R.id.menu_done);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_select:
                setSharedPreferencesValue(true);
                return true;
            case R.id.menu_preview:
                return true;
            case R.id.menu_done:
                return true;
            case android.R.id.home:
                onBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_gallery_select_key))){
            if(sharedPreferences.getBoolean(getString(R.string.pref_gallery_select_key), false)){
                changeModeForSelect();
            }else{
                changeModeForDefault();
            }
        }
    }

    @Override
    public void onBack() {
        setSharedPreferencesValue(false);
    }

    private void setSharedPreferencesValue(boolean value){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(getString(R.string.pref_gallery_select_key), value);
        editor.commit();
    }

    @Override
    public void changeModeForSelect() {
        //menu_select menu item gone.
        //menu_preview, menu_done menu item visible.
        mSelectMenuItem.setVisible(false);
        mPreviewMenuItem.setVisible(true);
        mDoneMenuItem.setVisible(true);

        //toolbar title change.
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.title_select));
        //toolbar home button gone.
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set listener
        ((MainActivity)getActivity()).setOnBackKeyPressedListener(this);
    }

    @Override
    public void changeModeForDefault() {
        //menu_select menu item visible.
        //menu_preview, menu_done menu item gone.
        mSelectMenuItem.setVisible(true);
        mPreviewMenuItem.setVisible(false);
        mDoneMenuItem.setVisible(false);

        //toolbar title change.
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.title_gallery));
        //toolbar home button visible.
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //set listener
        ((MainActivity)getActivity()).setOnBackKeyPressedListener(null);
        mFolderListPresenter.updateImageList();
    }

}
