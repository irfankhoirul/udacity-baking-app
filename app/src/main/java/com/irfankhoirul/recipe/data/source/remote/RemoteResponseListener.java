package com.irfankhoirul.recipe.data.source.remote;

import java.util.ArrayList;

import retrofit2.Call;

public interface RemoteResponseListener<T> {
    void onRequestStart(Call<T> call);

    void onSuccess(ArrayList<T> result);

    void onFailure(Throwable throwable);
}
