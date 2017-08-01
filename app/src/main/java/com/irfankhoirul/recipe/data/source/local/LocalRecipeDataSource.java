package com.irfankhoirul.recipe.data.source.local;

import android.database.Cursor;
import android.net.Uri;

import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.data.source.RecipeDataSource;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 7/24/2017.
 */

public interface LocalRecipeDataSource extends RecipeDataSource {
    void getAll(LocalDataObserver<Cursor> cursorFavoriteDataObserver);

    void getById(long id, LocalDataObserver<Cursor> cursorFavoriteDataObserver);

    void insert(Recipe recipe, LocalDataObserver<Uri> uriFavoriteDataObserver);

    void insertMany(ArrayList<Recipe> recipes, LocalDataObserver<Uri> uriFavoriteDataObserver);

    void update(Recipe recipe, LocalDataObserver<Integer> integerFavoriteDataObserver);

    void delete(long id, LocalDataObserver<Integer> integerFavoriteDataObserver);
}