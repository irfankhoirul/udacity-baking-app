package com.irfankhoirul.recipe.modul.widget;

import android.app.Application;
import android.util.Log;

import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.data.source.local.LocalDataObserver;
import com.irfankhoirul.recipe.data.source.local.LocalRecipeDataSource;
import com.irfankhoirul.recipe.data.source.local.LocalRecipeDataSourceImpl;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

/**
 * Created by Irfan Khoirul on 8/9/2017.
 */

public class RecipeChoiceDialogPresenter implements RecipeChoiceContract.Presenter {

    private final RecipeChoiceContract.View view;
    private final LocalRecipeDataSource localRecipeDataSource;
    private ArrayList<Recipe> recipes = new ArrayList<>();

    public RecipeChoiceDialogPresenter(Application application, RecipeChoiceContract.View view) {
        this.view = view;
        this.localRecipeDataSource = new LocalRecipeDataSourceImpl(application.getApplicationContext());
    }

    @Override
    public void loadRecipes() {
        if (recipes.size() == 0) {
            Log.v("StartLoadRecipe", "True");
            // Try to Get From Cache First
            localRecipeDataSource.getAll(new LocalDataObserver<ArrayList<Recipe>>() {
                @Override
                public void onNext(@NonNull ArrayList<Recipe> cachedRecipes) {
                    Log.v("LoadRecipe", "OnNext");
                    if (cachedRecipes != null && cachedRecipes.size() > 0) {
                        recipes.clear();
                        recipes.addAll(cachedRecipes);
                        Log.v("RecipeLoaded", String.valueOf(cachedRecipes.size()));
                        view.updateRecipeList();
                    } else {
                        Log.v("Recipe", "IsEmpty");
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    super.onError(e);
                    Log.v("LoadRecipe", "OnError");
                }
            });
        } else {
            Log.v("Recipe", "NotHaveToBeLoaded");
            view.updateRecipeList();
        }
    }

    @Override
    public ArrayList<Recipe> getLoadedRecipes() {
        return recipes;
    }

}
