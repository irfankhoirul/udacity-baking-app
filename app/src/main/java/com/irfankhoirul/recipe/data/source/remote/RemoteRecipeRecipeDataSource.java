package com.irfankhoirul.recipe.data.source.remote;

import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.data.source.RecipeDataSource;
import com.irfankhoirul.recipe.data.source.RequestResponseListener;

/**
 * Created by Irfan Khoirul on 7/24/2017.
 */

public interface RemoteRecipeRecipeDataSource extends RecipeDataSource {
    void getRecipes(RequestResponseListener<Recipe> responseListener);
}
