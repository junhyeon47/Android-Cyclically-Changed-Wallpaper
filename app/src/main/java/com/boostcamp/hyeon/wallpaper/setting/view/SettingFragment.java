package com.boostcamp.hyeon.wallpaper.setting.view;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.Image;
import com.boostcamp.hyeon.wallpaper.base.domain.Wallpaper;
import com.boostcamp.hyeon.wallpaper.base.util.AlarmManagerHelper;
import com.boostcamp.hyeon.wallpaper.base.util.Define;
import com.boostcamp.hyeon.wallpaper.base.util.SharedPreferenceHelper;
import com.boostcamp.hyeon.wallpaper.setting.adapter.WallpaperListAdapter;
import com.boostcamp.hyeon.wallpaper.setting.presenter.SettingPresenter;
import com.boostcamp.hyeon.wallpaper.setting.presenter.SettingPresenterImpl;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements SettingPresenter.View, SwitchCompat.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener{
    private static final String TAG = SettingFragment.class.getSimpleName();
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.tv_wallpaper_type) TextView mWallpaperTypeTextView;
    @BindView(R.id.tv_wallpaper_change_cycle) TextView mWallpaperChangeCycleTextView;
    @BindView(R.id.tv_random_wallpaper) TextView mRandomWallpaperTextView;
    @BindView(R.id.tv_transparent_wallpaper) TextView mTransparentWallpaperTextView;
    @BindView(R.id.sw_wallpaper_type) SwitchCompat mWallpaperTypeSwitch;
    @BindView(R.id.sw_random_wallpaper) SwitchCompat mRandomWallpaperSwitch;
    @BindView(R.id.sw_transparent_wallpaper) SwitchCompat mTransparentSwitch;
    @BindView(R.id.layout_recycler_view) LinearLayout mRecyclerViewLinearLayout;
    @BindView(R.id.tv_wallpaper_is_using) TextView mWallpaperIsUsingTextView;
    @BindView(R.id.view_shadow) View mShadowView;
    private WallpaperListAdapter mAdapter;
    private SettingPresenterImpl mSettingPresenter;
    private RadioGroup mChangeRepeatCycleRadioGroup;
    private int mChangeCycle;

    public SettingFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);

        init();
        return view;
    }

    private void init(){
        //init adapter
        Realm realm = WallpaperApplication.getRealmInstance();
        realm.beginTransaction();

        RealmResults<Image> imageRealmResults = null;
        Wallpaper wallpaper= realm.where(Wallpaper.class).findFirst();
        if(wallpaper != null){
            imageRealmResults = realm.where(Wallpaper.class).findFirst().getImages().sort("number", Sort.ASCENDING);
        }
        mAdapter = new WallpaperListAdapter(
                getContext(),
                imageRealmResults,
                true
        );
        realm.commitTransaction();

        //init presenter
        mSettingPresenter = new SettingPresenterImpl();
        mSettingPresenter.attachView(this);
        mSettingPresenter.setListAdapterModel(mAdapter);
        mSettingPresenter.setListAdapterView(mAdapter);

        //init recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));

        if(imageRealmResults == null){
            mRecyclerViewLinearLayout.setVisibility(View.GONE);
            mShadowView.setVisibility(View.VISIBLE);
        }

        setSettingValues(true);
        //init listener
        mWallpaperTypeSwitch.setOnCheckedChangeListener(this);
        mRandomWallpaperSwitch.setOnCheckedChangeListener(this);
        //mTransparentSwitch.setOnCheckedChangeListener(this);

        Log.d(TAG, "init()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    //switch group checked change.
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "onCheckedChanged: "+isChecked);
        if(buttonView.equals(mWallpaperTypeSwitch)){
            Log.d(TAG, "onCheckedChanged - mWallpaperTypeSwitch: "+isChecked);
            SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.BOOLEAN_IS_USING_WALLPAPER, isChecked);
        }else if(buttonView.equals(mRandomWallpaperSwitch)){
            SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.BOOLEAN_IS_RANDOM_ORDER, isChecked);
        }else if(buttonView.equals(mTransparentSwitch)){
//            SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.BOOLEAN_IS_TRANSPARENT_WALLPAPER, isChecked);
//            Calendar date = Calendar.getInstance();
//            date.setTimeInMillis(System.currentTimeMillis() + Define.DELAY_MILLIS);
//            if(isChecked) {
//                AlarmManagerHelper.unregisterToAlarmManager(getContext(), Define.ID_ALARM_DEFAULT);
//                AlarmManagerHelper.registerToAlarmManager(getContext(), date, Define.ID_ALARM_TRANSPARENT);
//            }else {
//                AlarmManagerHelper.unregisterToAlarmManager(getContext(), Define.ID_ALARM_TRANSPARENT);
//                AlarmManagerHelper.registerToAlarmManager(getContext(), date, Define.ID_ALARM_DEFAULT);
//            }
        }
        setSettingValues(false);
    }

    @OnClick(R.id.layout_wallpaper_change_cycle)
    public void showAlertDialogForWallpaperChangeCycle(){
        if(mWallpaperChangeCycleTextView.getText().equals(getString(R.string.label_not_using)))
            return;
        onPause();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.item_menu_done_alert);
        builder.setPositiveButton("설정 완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setWallpaperChangeCycle();
            }
        });
        builder.setNegativeButton(getString(R.string.label_cancel), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((Dialog)dialog).findViewById(R.id.layout_change_screen).setVisibility(View.GONE);
                mChangeRepeatCycleRadioGroup = (RadioGroup)((Dialog)dialog).findViewById(R.id.rb_change_repeat_cycle);
                mChangeRepeatCycleRadioGroup.setOnCheckedChangeListener(SettingFragment.this);
                mChangeRepeatCycleRadioGroup.check(mChangeRepeatCycleRadioGroup.getChildAt(0).getId());
                for(int i=0; i<mChangeRepeatCycleRadioGroup.getChildCount(); ++i){
                    AppCompatRadioButton radioButton = (AppCompatRadioButton)mChangeRepeatCycleRadioGroup.getChildAt(i);
                    String radioButtonValue = radioButton.getText().toString();
                    if(radioButtonValue.equals(mWallpaperChangeCycleTextView.getText().toString())){
                        mChangeRepeatCycleRadioGroup.check(radioButton.getId());
                        break;
                    }
                }
            }
        });
        alertDialog.show();
    }

    public void setWallpaperChangeCycle(){
        SharedPreferenceHelper.getInstance().put(SharedPreferenceHelper.Key.LONG_REPEAT_CYCLE_MILLS, (long) mChangeCycle);
        if(mChangeCycle != Define.CHANGE_CYCLE_SCREEN_OFF){
            AlarmManagerHelper.unregisterToAlarmManager(getContext(), Define.ID_ALARM_DEFAULT);
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(System.currentTimeMillis() + Define.DELAY_MILLIS);
            AlarmManagerHelper.registerToAlarmManager(getContext(),date, Define.ID_ALARM_DEFAULT);
        }
        setWallpaperChangeCycle(mChangeCycle);
    }

    //radio group checked change.
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
        if(radioButton == null)
            return;
        if(group.equals(mChangeRepeatCycleRadioGroup) && radioButton.isChecked()){
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
            Log.d(TAG, "change cycle: "+mChangeCycle);
        }
    }

    private void setSettingValues(boolean isInit){
        //get setting values init view
        boolean isUsingWallpaper = SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_IS_USING_WALLPAPER, false);
        Log.d(TAG, "setSettingValues - isUsingWallpaper: "+ isUsingWallpaper);
        long changeCycle = SharedPreferenceHelper.getInstance().getLong(SharedPreferenceHelper.Key.LONG_REPEAT_CYCLE_MILLS, Define.NOT_USE_CHANGE_CYCLE);
        boolean isRandomOrder = SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_IS_RANDOM_ORDER, false);
        //boolean isTransparentWallpaper = SharedPreferenceHelper.getInstance().getBoolean(SharedPreferenceHelper.Key.BOOLEAN_IS_TRANSPARENT_WALLPAPER, false);
        if(isInit){
            mWallpaperTypeSwitch.setChecked(isUsingWallpaper);
            mRandomWallpaperSwitch.setChecked(isRandomOrder);
            //mTransparentSwitch.setChecked(isTransparentWallpaper);
        }
        setWallpaperChangeType(isUsingWallpaper);
        setWallpaperChangeCycle(changeCycle);
        setWallpaperRandomOrder(isRandomOrder);
        //setTransparentWallpaper(isTransparentWallpaper);
    }

    private void setWallpaperChangeType(boolean value){
        if(value){
            int changeScreenType = SharedPreferenceHelper.getInstance().getInt(SharedPreferenceHelper.Key.INT_CHANGE_SCREEN_TYPE, 0);
            Log.d(TAG, "changeScreenType: "+changeScreenType);
            mWallpaperIsUsingTextView.setText(getString(R.string.label_wallpaper_change_using));
            if(changeScreenType != 0){
                mWallpaperTypeTextView.setText(getString(changeScreenType));
                mRecyclerViewLinearLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mShadowView.setVisibility(View.GONE);
            }else{
                mWallpaperTypeTextView.setText(getString(R.string.label_setup_wallpaper));
                mRecyclerViewLinearLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mShadowView.setVisibility(View.VISIBLE);
            }
            mRandomWallpaperSwitch.setEnabled(true);
            //mTransparentSwitch.setEnabled(true);
        }else{
            mWallpaperIsUsingTextView.setText(getString(R.string.label_wallpaper_change_not_using));
            mWallpaperTypeTextView.setText("");
            mRecyclerViewLinearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mShadowView.setVisibility(View.VISIBLE);
            mRandomWallpaperSwitch.setEnabled(false);
            //mTransparentSwitch.setEnabled(false);
        }
    }

    private void setWallpaperChangeCycle(long changeCycle){
        String changeCycleConvertToTime;
        if(changeCycle == Define.NOT_USE_CHANGE_CYCLE){
            changeCycleConvertToTime = getString(R.string.label_not_using);
        }else {
            //mills to minute or hour.
            if(changeCycle == Define.CHANGE_CYCLE_SCREEN_OFF){
                changeCycleConvertToTime = getString(R.string.label_screen_on_change_wallpaper);
            }else if(changeCycle / Define.HOUR_CONVERT_TO_MILLIS == 0){
                changeCycleConvertToTime = String.valueOf(changeCycle / Define.MINUTE_CONVERT_TO_MILLIS)+getString(R.string.label_minute);
            }else{
                changeCycleConvertToTime = String.valueOf(changeCycle / Define.HOUR_CONVERT_TO_MILLIS)+getString(R.string.label_hour);
            }
        }
        if(mWallpaperTypeSwitch.isChecked() && !mTransparentSwitch.isChecked()) {
            mWallpaperChangeCycleTextView.setText(changeCycleConvertToTime);
        }else{
            mWallpaperChangeCycleTextView.setText(getString(R.string.label_not_using));
        }
    }

    private void setWallpaperRandomOrder(boolean value){
        if(mRandomWallpaperSwitch.isEnabled() && !mTransparentSwitch.isChecked()) {
            if (value) {
                mRandomWallpaperTextView.setText(getString(R.string.label_using));
            } else {
                mRandomWallpaperTextView.setText(getString(R.string.label_not_using));
            }
        }else{
            mRandomWallpaperTextView.setText(getString(R.string.label_not_using));
        }
    }

    private void setTransparentWallpaper(boolean value){
        if(mTransparentSwitch.isEnabled()) {
            if (value) {
                mTransparentWallpaperTextView.setText(getString(R.string.label_using));
                mRandomWallpaperSwitch.setEnabled(false);
                mRecyclerViewLinearLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mShadowView.setVisibility(View.VISIBLE);
            } else {
                mTransparentWallpaperTextView.setText(getString(R.string.label_not_using));
                mRandomWallpaperSwitch.setEnabled(true);
                mRecyclerViewLinearLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mShadowView.setVisibility(View.GONE);
            }

        }else{
            mTransparentWallpaperTextView.setText(getString(R.string.label_not_using));
        }
    }
}
