package com.boostcamp.hyeon.wallpaper.search.model;

import android.content.Context;
import android.util.Log;

import com.boostcamp.hyeon.wallpaper.R;
import com.boostcamp.hyeon.wallpaper.base.app.WallpaperApplication;
import com.boostcamp.hyeon.wallpaper.base.domain.ImageNaver;
import com.boostcamp.hyeon.wallpaper.base.domain.ResultNaver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

/**
 * Created by hyeon on 2017. 2. 21..
 */

public class SearchModel {
    private static final String TAG = SearchModel.class.getSimpleName();
    private Context mContext;
    private ModelDataChange mModelDataChange;

    public SearchModel(Context mContext) {
        this.mContext = mContext;
    }

    interface NaverSearch{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        @GET("search/image")
        Call<ResultNaver> getList(
                @HeaderMap Map<String, String> headers,
                @Query("query") String query,
                @Query("start") Integer start,
                @Query("display") Integer display,
                @Query("sort") String sort
        );
    }

    public interface ModelDataChange {
        void update();
    }

    public void setOnChangeListener(ModelDataChange dataChange) {
        this.mModelDataChange = dataChange;
    }

    public void callGetList(String query, int start){
        NaverSearch naverSearch = NaverSearch.retrofit.create(NaverSearch.class);
        Map<String, String> map = new HashMap<>();
        map.put("X-Naver-Client-Id", mContext.getString(R.string.naver_client_id));
        map.put("X-Naver-Client-Secret", mContext.getString(R.string.naver_client_secret));
        Call<ResultNaver> call = naverSearch.getList(map, query, start, 100, "sim");
        call.enqueue(callBackListener);
    }

    private Callback<ResultNaver> callBackListener = new Callback<ResultNaver>() {
        @Override
        public void onResponse(Call<ResultNaver> call, Response<ResultNaver> response) {
            Log.d(TAG, "onResponse");
            if (response.isSuccessful()) {
                List<ImageNaver> imageNaverList = response.body().getItems();
                Log.d(TAG, "Success");
                Realm realm = WallpaperApplication.getRealmInstance();
                realm.beginTransaction();
                for(ImageNaver item : imageNaverList){
                    ImageNaver imageNaver = realm.createObject(ImageNaver.class);
                    imageNaver.setLink(item.getLink());
                    imageNaver.setThumbnail(item.getThumbnail());
                    imageNaver.setSizeWidth(item.getSizeWidth());
                    imageNaver.setSizeHeight(item.getSizeHeight());
                }
                realm.commitTransaction();
                if(mModelDataChange != null){
                    mModelDataChange.update();
                }
            }
        }

        @Override
        public void onFailure(Call<ResultNaver> call, Throwable t) {
            t.printStackTrace();
        }
    };
}
