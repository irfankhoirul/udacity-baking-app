package com.irfankhoirul.recipe.modul.widget;

import com.irfankhoirul.recipe.data.pojo.Recipe;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 8/10/2017.
 */

public interface RecipeChoiceContract {
    interface View {
        void updateRecipeList();
    }

    interface Presenter {
        void loadRecipes();

        ArrayList<Recipe> getLoadedRecipes();
    }
}
