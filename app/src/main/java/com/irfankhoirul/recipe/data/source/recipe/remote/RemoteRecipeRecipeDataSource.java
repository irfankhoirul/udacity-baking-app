package com.irfankhoirul.recipe.data.source.recipe.remote;

import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.data.source.recipe.RecipeDataSource;
import com.irfankhoirul.recipe.data.source.recipe.RequestResponseListener;

/**
 * Created by Irfan Khoirul on 7/24/2017.
 */

public interface RemoteRecipeRecipeDataSource extends RecipeDataSource {
    void getRecipes(RequestResponseListener<Recipe> responseListener);
}
