package com.irfankhoirul.recipe.data.source.recipe.remote;

import com.irfankhoirul.recipe.data.pojo.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

public interface EndPoints {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getRecipes();

}
