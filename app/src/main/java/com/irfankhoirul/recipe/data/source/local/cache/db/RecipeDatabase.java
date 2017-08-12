package com.irfankhoirul.recipe.data.source.local.cache.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.irfankhoirul.recipe.data.pojo.Ingredient;
import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.data.pojo.Step;
import com.irfankhoirul.recipe.data.source.local.cache.db.dao.IngredientDao;
import com.irfankhoirul.recipe.data.source.local.cache.db.dao.RecipeDao;
import com.irfankhoirul.recipe.data.source.local.cache.db.dao.StepDao;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {

    private static RecipeDatabase sInstance;

    public static synchronized RecipeDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), RecipeDatabase.class,
                            "recipe")
                    .build();
        }
        return sInstance;
    }

    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                RecipeDatabase.class).build();
    }

    @SuppressWarnings("WeakerAccess")
    public abstract RecipeDao recipeDao();

    @SuppressWarnings("WeakerAccess")
    public abstract IngredientDao ingredientDao();

    @SuppressWarnings("WeakerAccess")
    public abstract StepDao stepDao();
}
