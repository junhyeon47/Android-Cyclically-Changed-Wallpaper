package com.boostcamp.hyeon.wallpaper.gallery.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Folder;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.domain.Wallpaper;
import com.boostcamp.hyeon.wallpaper.base.util.Define;
import com.boostcamp.hyeon.wallpaper.base.util.GridItemDecoration;
import com.boostcamp.hyeon.wallpaper.base.util.RegisterWallpaperAsyncTask;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;
import com.boostcamp.hyeon.wallpaper.detail.view.DetailActivity;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.FolderListAdapter;
import com.boostcamp.hyeon.wallpaper.gallery.adapter.ImageListAdapter;
import com.boostcamp.hyeon.wallpaper.gallery.model.GalleryModel;
import com.boostcamp.hyeon.wallpaper.gallery.presenter.FolderListPresenter;
import com.boostcamp.hyeon.wallpaper.gallery.presenter.FolderListPresenterImpl;
import com.boostcamp.hyeon.wallpaper.gallery.presenter.ImageListPresenter;
import com.boostcamp.hyeon.wallpaper.gallery.presenter.ImageListPresenterImpl;
import com.boostcamp.hyeon.wallpaper.base.listener.OnBackKeyPressedListener;
import com.boostcamp.hyeon.wallpaper.main.view.MainActivity;
import com.boostcamp.hyeon.wallpaper.preview.view.PreviewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment implements FolderListPresenter.View, ImageListPresenter.View, OnBackKeyPressedListener, RadioGroup.OnCheckedChangeListener{
    private static final String TAG = GalleryFragment.class.getSimpleName();
    @BindView(R.id.rv_folder) RecyclerView mFolderRecyclerView;
    @BindView(R.id.rv_image) RecyclerView mImageRecyclerView;
    private FolderListAdapter mFolderListAdapter;
    private ImageListAdapter mImageListAdapter;
    private FolderListPresenterImpl mFolderListPresenter;
    private ImageListPresenterImpl mImageListPresenter;
    private MenuItem mSelectMenuItem, mPreviewMenuItem, mDoneMenuItem;
    private RadioGroup mChangeScreenRadioGroup, mChangeRepeatCycleRadioGroup;
    private int mChangeCycle;
    private int mChangeScreenType;

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
        //init Model
        GalleryModel model = new GalleryModel();

        //init Folder Adapter
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        RealmResults<Folder> folderRealmResults = realm.where(Folder.class).findAllSorted("name", Sort.ASCENDING);
        if(folderRealmResults.size() != 0){
            folderRealmResults.get(0).setOpened(true);
        }
        mFolderListAdapter = new FolderListAdapter(
                getContext(),
                folderRealmResults,
                true
        );
        realm.commitTransaction();

        mFolderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFolderRecyclerView.setAdapter(mFolderListAdapter);
        mFolderRecyclerView.setHasFixedSize(false);

        //init Folder Presenter
        mFolderListPresenter = new FolderListPresenterImpl(model);
        mFolderListPresenter.attachView(this);
        mFolderListPresenter.setListAdapterModel(mFolderListAdapter);
        mFolderListPresenter.setListAdapterView(mFolderListAdapter);

        //init Image Adapter
        realm.beginTransaction();
        String bucketId;
        RealmResults<Image> imageRealmResults = null;
        if(folderRealmResults.size() != 0) {
            bucketId = realm.where(Folder.class).equalTo("isOpened", true).findFirst().getBucketId();
            imageRealmResults = realm.where(Folder.class).equalTo("bucketId", bucketId).findFirst().getImages().sort("dateAdded", Sort.DESCENDING);
        }
        mImageListAdapter = new ImageListAdapter(
                getContext(),
                imageRealmResults,
                true
        );
        realm.commitTransaction();

        //init Image(Right) RecyclerView
        mImageRecyclerView.addItemDecoration(new GridItemDecoration(1));
        mImageRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mImageRecyclerView.setAdapter(mImageListAdapter);
        mImageRecyclerView.setHasFixedSize(false);

        //init Presenter
        mImageListPresenter = new ImageListPresenterImpl(model);
        mImageListPresenter.attachView(this);
        mImageListPresenter.setListAdapterModel(mImageListAdapter);
        mImageListPresenter.setListAdapterView(mImageListAdapter);

        //init SharedPreferences
        SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.BOOLEAN_GALLEY_SELECT_MODE, false);

        //init initial Value
        mChangeCycle = Define.DEFAULT_CHANGE_CYCLE;
        mChangeScreenType = Define.DEFAULT_CHANGE_SCREEN_TYPE;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "on Resume");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "on Pause");
        if(SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_GALLEY_SELECT_MODE, false)){
            if(!SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_PREVIEW_ACTIVITY_CALL, false))
                changeModeForDefault();
        }
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
                moveToPreviewActivity();
                return true;
            case R.id.menu_done:
                clickDone();
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
        mImageRecyclerView.getRecycledViewPool().clear();
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
        mImageListAdapter.notifyAdapter();
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

        mImageListPresenter.updateAllImagesDeselected();
    }

    @Override
    public void clickFolder(int position) {
        String bucketId = mFolderListAdapter.getData().get(position).getBucketId();
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        RealmResults<Image> imageRealmResults = realm.where(Image.class).equalTo("bucketId", bucketId).findAllSorted("dateAdded", Sort.DESCENDING);
        realm.commitTransaction();
        //change adapter
        mImageListAdapter = new ImageListAdapter(
                getContext(),
                imageRealmResults,
                true
        );

        mImageRecyclerView.setAdapter(mImageListAdapter);

        mImageListPresenter.detachView();
        mImageListPresenter.attachView(this);
        mImageListPresenter.setListAdapterModel(mImageListAdapter);
        mImageListPresenter.setListAdapterView(mImageListAdapter);
    }

    @Override
    public void moveToDetailActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtras(bundle);

        getActivity().startActivity(intent);
    }

    private void moveToPreviewActivity(){
        SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.BOOLEAN_PREVIEW_ACTIVITY_CALL, true);
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();

        if(realm.where(Image.class).equalTo("isSelected", true).findAll().size() > 0) {
            Intent intent = new Intent(getActivity(), PreviewActivity.class);
            getActivity().startActivity(intent);
        }

        realm.commitTransaction();
    }

    private void registerWallpaper(){
        new RegisterWallpaperAsyncTask(getActivity(), this).execute(mChangeCycle, mChangeScreenType);
    }

    private void clickDone(){
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();
        int numberOfSelectedImage = realm.where(Image.class).equalTo("isSelected", true).findAll().size();
        realm.commitTransaction();

        if(numberOfSelectedImage == 0){
            Toast.makeText(getActivity(), "이미지를 선택해주세요!", Toast.LENGTH_SHORT).show();
        }else if(numberOfSelectedImage == 1){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                registerWallpaper();
            }else{
                showAlertDialog(numberOfSelectedImage);
            }
        }else if(numberOfSelectedImage > 1){
            showAlertDialog(numberOfSelectedImage);
        }
    }

    private void showAlertDialog(final int numberOfSelectedImage){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.item_menu_done_alert);
        builder.setPositiveButton(getString(R.string.label_wallpaper_register), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registerWallpaper();
            }
        });
        builder.setNegativeButton(getString(R.string.label_cancel), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N && numberOfSelectedImage > 1){
                    //if build version is less than nougat and selected images is greater than one.
                    ((Dialog)dialog).findViewById(R.id.layout_change_screen).setVisibility(View.GONE);
                    mChangeRepeatCycleRadioGroup = (RadioGroup)((Dialog)dialog).findViewById(R.id.rb_change_repeat_cycle);
                    mChangeRepeatCycleRadioGroup.setOnCheckedChangeListener(GalleryFragment.this);
                    mChangeRepeatCycleRadioGroup.check(mChangeRepeatCycleRadioGroup.getChildAt(0).getId());
                    //SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.INT_CHANGE_SCREEN_TYPE, Define.CHANGE_SCREEN_TYPE[0]);
                }else if(Build.VERSION.SDK_INT == Build.VERSION_CODES.N && numberOfSelectedImage == 1){
                    //if build version is nougat and selected images is only one.
                    ((Dialog)dialog).findViewById(R.id.layout_change_repeat_cycle).setVisibility(View.GONE);
                    mChangeScreenRadioGroup = (RadioGroup)((Dialog)dialog).findViewById(R.id.rb_change_screen);
                    mChangeScreenRadioGroup.setOnCheckedChangeListener(GalleryFragment.this);
                    mChangeScreenRadioGroup.check(mChangeScreenRadioGroup.getChildAt(0).getId());
                    //SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.LONG_REPEAT_CYCLE_MILLS, Define.NOT_USE_CHANGE_CYCLE);
                }else{
                    //if build version is nougat and selected selected images is greater than one.
                    mChangeScreenRadioGroup = (RadioGroup)((Dialog)dialog).findViewById(R.id.rb_change_screen);
                    mChangeRepeatCycleRadioGroup = (RadioGroup)((Dialog)dialog).findViewById(R.id.rb_change_repeat_cycle);
                    mChangeScreenRadioGroup.setOnCheckedChangeListener(GalleryFragment.this);
                    mChangeRepeatCycleRadioGroup.setOnCheckedChangeListener(GalleryFragment.this);
                    mChangeScreenRadioGroup.check(mChangeScreenRadioGroup.getChildAt(0).getId());
                    mChangeRepeatCycleRadioGroup.check(mChangeRepeatCycleRadioGroup.getChildAt(0).getId());
                }
            }
        });
        alertDialog.show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
        if(radioButton == null)
            return;
        if(group.equals(mChangeScreenRadioGroup) && radioButton.isChecked()){
            Log.d(TAG, getString(Define.CHANGE_SCREEN_TYPE[group.indexOfChild(radioButton)]));
            mChangeScreenType = Define.CHANGE_SCREEN_TYPE[group.indexOfChild(radioButton)];
        }else if(group.equals(mChangeRepeatCycleRadioGroup) && radioButton.isChecked()){
            String value = radioButton.getText().toString();
            int minute = value.indexOf(getString(R.string.label_minute));
            int hour = value.indexOf(getString(R.string.label_hour));

            if(minute != -1 && hour == -1){
                mChangeCycle = Integer.valueOf(value.substring(0, minute));
                mChangeCycle *= Define.MINUTE_CONVERT_TO_MILLIS;
            }else if(minute == -1 && hour !=-1){
                mChangeCycle = Integer.valueOf(value.substring(0, hour));
                mChangeCycle *= Define.HOUR_CONVERT_TO_MILLIS;
            }else{
                mChangeCycle = Define.CHANGE_CYCLE_SCREEN_OFF;
            }
            Log.d(TAG, "mRepeatCycle: "+mChangeCycle);
        }
    }
}
