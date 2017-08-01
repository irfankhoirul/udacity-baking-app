package com.irfankhoirul.recipe.data.source.remote;

import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.data.source.RecipeDataSource;

/**
 * Created by Irfan Khoirul on 7/24/2017.
 */

public interface RemoteRecipeDataSource extends RecipeDataSource {
    void getRecipes(RemoteResponseListener<Recipe> responseListener);
}
