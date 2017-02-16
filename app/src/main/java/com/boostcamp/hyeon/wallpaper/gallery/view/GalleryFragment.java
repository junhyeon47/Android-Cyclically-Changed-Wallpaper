package com.boostcamp.hyeon.wallpaper.gallery.view;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Folder;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;
import com.boostcamp.hyeon.wallpaper.base.util.SyncDataHelper;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.FolderListAdapter;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.ImageListAdapter;
import com.boostcamp.hyeon.wallpaper.gallery.model.GalleryModel;
import com.boostcamp.hyeon.wallpaper.gallery.presenter.FolderListPresenter;
import com.boostcamp.hyeon.wallpaper.gallery.presenter.FolderListPresenterImpl;
import com.boostcamp.hyeon.wallpaper.gallery.presenter.ImageListPresenter;
import com.boostcamp.hyeon.wallpaper.gallery.presenter.ImageListPresenterImpl;
import com.boostcamp.hyeon.wallpaper.listener.OnBackKeyPressedListener;
import com.boostcamp.hyeon.wallpaper.main.view.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment implements FolderListPresenter.View, ImageListPresenter.View, OnBackKeyPressedListener, LoaderManager.LoaderCallbacks<Void>{
    private static final String TAG = GalleryFragment.class.getSimpleName();
    private static final int SYNC_DATA_LOADER = 99;
    @BindView(R.id.rv_folder) RecyclerView mFolderRecyclerView;
    @BindView(R.id.rv_image) RecyclerView mImageRecyclerView;
    private FolderListAdapter mFolderListAdapter;
    private FolderListPresenterImpl mFolderListPresenter;
    private ImageListPresenterImpl mImageListPresenter;
    private MenuItem mSelectMenuItem, mPreviewMenuItem, mDoneMenuItem;


    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        //init loader
        getLoaderManager().initLoader(SYNC_DATA_LOADER, null, this);

        //init Model
        GalleryModel model = new GalleryModel();

        //init Folder Adapter
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        mFolderListAdapter = new FolderListAdapter(
                getContext(),
                realm.where(Folder.class).findAllSorted("name", Sort.ASCENDING),
                true
        );
        realm.commitTransaction();


        //init Folder(Left) RecyclerView
        ViewGroup.LayoutParams params = mFolderRecyclerView.getLayoutParams();
        params.width =  ((WallpaperApplication)getContext().getApplicationContext()).mDeviceWidthSize/3;
        mFolderRecyclerView.setLayoutParams(params);
        mFolderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFolderRecyclerView.setAdapter(mFolderListAdapter);
        mFolderRecyclerView.setHasFixedSize(true);

        //init Folder Presenter
        mFolderListPresenter = new FolderListPresenterImpl(model);
        mFolderListPresenter.attachView(this);
        mFolderListPresenter.setFolderListAdapterView(mFolderListAdapter);
        mFolderListPresenter.setFolderListAdapterModel(mFolderListAdapter);

        //init Image Adapter
        realm.beginTransaction();
        ImageListAdapter imageListAdapter = new ImageListAdapter(
                getContext(),
                realm.where(Folder.class).equalTo("bucketId", mFolderListPresenter.getOpenedFolderId()).findFirst().getImages(),
                true
        );
        realm.commitTransaction();

        //init Image(Right) RecyclerView
        mImageRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mImageRecyclerView.setAdapter(imageListAdapter);
        mImageRecyclerView.setHasFixedSize(true);

        //init Presenter
        mImageListPresenter = new ImageListPresenterImpl(model);
        mImageListPresenter.attachView(this);
        mImageListPresenter.setImageListAdapterModel(imageListAdapter);
        mImageListPresenter.setImageListAdapterView(imageListAdapter);

        //init SharedPreferences
        SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.BOOLEAN_GALLEY_SELECT_MODE, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "on Resume");
        //init LoaderManager
        LoaderManager loaderManager = getLoaderManager();
        Loader<Void> loader = loaderManager.getLoader(SYNC_DATA_LOADER);
        if(loader == null){
            loaderManager.initLoader(SYNC_DATA_LOADER, null, this);
        }else{
            loaderManager.restartLoader(SYNC_DATA_LOADER, null, this);
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "on Pause");
        if(SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_GALLEY_SELECT_MODE, true))
            changeModeForDefault();
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
                changeModeForSelect();
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
    public void onBack() {
        if(SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_GALLEY_SELECT_MODE, false))
            changeModeForDefault();
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
        //set SharedPreferences
        SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.BOOLEAN_GALLEY_SELECT_MODE, true);
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
        //set SharedPreferences
        SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.BOOLEAN_GALLEY_SELECT_MODE, false);

        //update all Image objects isSelected set false
        mImageListPresenter.updateAllImagesDeselected();
    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(getActivity()) {
            @Override
            protected void onStartLoading() {
                if(SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_FIRST_INIT, true)){
                    SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.BOOLEAN_FIRST_INIT, false);
                }else{
                    forceLoad();
                }
            }

            @Override
            public Void loadInBackground() {
                //content provider data sync
                Log.d(TAG, "called AsyncTask");
                SyncDataHelper.syncContentProviderToRealm(getContext(), null);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    @Override
    public void clickFolder(int position) {
        //change adapter
        ImageListAdapter imageListAdapter = new ImageListAdapter(
                getContext(),
                mFolderListAdapter.getData().get(position).getImages(),
                true
        );
        mImageRecyclerView.setAdapter(imageListAdapter);

        mImageListPresenter.detachView();
        mImageListPresenter.attachView(this);
        mImageListPresenter.setImageListAdapterModel(imageListAdapter);
        mImageListPresenter.setImageListAdapterView(imageListAdapter);
    }
}
