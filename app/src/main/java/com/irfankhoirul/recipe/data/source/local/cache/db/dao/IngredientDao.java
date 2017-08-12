package com.irfankhoirul.recipe.data.source.local.cache.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.irfankhoirul.recipe.data.pojo.Ingredient;
import com.irfankhoirul.recipe.data.source.local.cache.db.RecipeDataContract;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

@Dao
public interface IngredientDao {
    @Query("SELECT COUNT(*) FROM " + RecipeDataContract.IngredientEntry.TABLE_NAME)
    int count();

    @Insert(onConflict = REPLACE)
    long insert(Ingredient ingredient);

    @Insert(onConflict = REPLACE)
    long[] insertAll(Ingredient[] ingredients);

    @Query("SELECT * FROM " + RecipeDataContract.IngredientEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.IngredientEntry.COLUMN_RECIPE_ID + " = :recipeId")
    Cursor selectAll(long recipeId);

    @Query("SELECT * FROM " + RecipeDataContract.IngredientEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.IngredientEntry.COLUMN_ID + " = :ingredientId" + " AND "
            + RecipeDataContract.IngredientEntry.COLUMN_RECIPE_ID + " = :recipeId")
    Cursor selectById(long ingredientId, long recipeId);

    @Query("DELETE FROM " + RecipeDataContract.IngredientEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.IngredientEntry.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Update(onConflict = REPLACE)
    int update(Ingredient ingredient);
}
