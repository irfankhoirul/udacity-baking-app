package com.irfankhoirul.recipe.data.source;

import java.util.ArrayList;

import retrofit2.Call;

public interface RequestResponseListener<T> {
    void onRequestStart(Call<T> call);

    void onSuccess(ArrayList<T> result);

    void onFailure(Throwable throwable);
}
