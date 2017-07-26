package com.irfankhoirul.recipe.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.data.source.RequestResponseListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

public class RemoteRecipeRecipeDataSourceImpl implements RemoteRecipeRecipeDataSource {

    private static final String TAG = RemoteRecipeRecipeDataSourceImpl.class.getSimpleName();
    private EndPoints endPoint;
    private CompositeDisposable mCompositeDisposable;

    public RemoteRecipeRecipeDataSourceImpl() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);

        String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        endPoint = retrofit.create(EndPoints.class);
    }

    @Override
    public void getRecipes(final RequestResponseListener<Recipe> responseListener) {
        Call<ArrayList<Recipe>> call = endPoint.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call,
                                   @NonNull Response<ArrayList<Recipe>> response) {
                if (response.body() == null) {
                    responseListener.onFailure(new Throwable());
                    Log.e("Fatal Error", "JSON Mal-formatted");
                } else {
                    responseListener.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call,
                                  @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
