package com.irfankhoirul.recipe.modul.recipe;

import android.support.annotation.Nullable;

import com.irfankhoirul.recipe.data.pojo.Recipe;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

public interface RecipeListContract {
    interface View {
        void updateRecipeList(ArrayList<Recipe> recipes);

        void setLoading(boolean status, @Nullable String message);

        void showError(String message);

        void updateRecipeList();
    }

    interface ViewModel {
        void loadRecipes(int source);

        ArrayList<Recipe> getLoadedRecipes();
    }
}
