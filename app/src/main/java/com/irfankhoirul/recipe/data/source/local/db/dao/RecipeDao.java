package com.irfankhoirul.recipe.data.source.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.data.source.local.db.RecipeDataContract;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

@Dao
public interface RecipeDao {

    @Query("SELECT COUNT(*) FROM " + RecipeDataContract.RecipeEntry.TABLE_NAME)
    int count();

    @Insert(onConflict = REPLACE)
    long insert(Recipe recipe);

    @Insert(onConflict = REPLACE)
    long[] insertAll(Recipe[] recipes);

    @Query("SELECT * FROM " + RecipeDataContract.RecipeEntry.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT " + RecipeDataContract.RecipeEntry.TABLE_NAME + ".*, " +
            RecipeDataContract.IngredientEntry.TABLE_NAME + ".*, " +
            RecipeDataContract.StepEntry.TABLE_NAME + ".* " +
            "FROM " + RecipeDataContract.RecipeEntry.TABLE_NAME + " " +
            "INNER JOIN " + RecipeDataContract.IngredientEntry.TABLE_NAME + " ON " +
            RecipeDataContract.IngredientEntry.TABLE_NAME + "." +
            RecipeDataContract.IngredientEntry.COLUMN_RECIPE_ID + " = " +
            RecipeDataContract.RecipeEntry.TABLE_NAME + "." + RecipeDataContract.RecipeEntry.COLUMN_ID + " " +
            "INNER JOIN " + RecipeDataContract.StepEntry.TABLE_NAME + " ON " +
            RecipeDataContract.StepEntry.TABLE_NAME + "." +
            RecipeDataContract.StepEntry.COLUMN_RECIPE_ID + " = " +
            RecipeDataContract.RecipeEntry.TABLE_NAME + "." + RecipeDataContract.RecipeEntry.COLUMN_ID)
    Cursor selectAllWithChildElements();

    @Query("SELECT * FROM " + RecipeDataContract.RecipeEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.RecipeEntry.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    @Query("SELECT " + RecipeDataContract.RecipeEntry.TABLE_NAME + ".*, " +
            RecipeDataContract.IngredientEntry.TABLE_NAME + ".*, " +
            RecipeDataContract.StepEntry.TABLE_NAME + ".* " +
            "FROM " + RecipeDataContract.RecipeEntry.TABLE_NAME + " " +
            "INNER JOIN " + RecipeDataContract.IngredientEntry.TABLE_NAME + " ON " +
            RecipeDataContract.IngredientEntry.TABLE_NAME + "." +
            RecipeDataContract.IngredientEntry.COLUMN_RECIPE_ID + " = " +
            RecipeDataContract.RecipeEntry.TABLE_NAME + "." + RecipeDataContract.RecipeEntry.COLUMN_ID + " " +
            "INNER JOIN " + RecipeDataContract.StepEntry.TABLE_NAME + " ON " +
            RecipeDataContract.StepEntry.TABLE_NAME + "." +
            RecipeDataContract.StepEntry.COLUMN_RECIPE_ID + " = " +
            RecipeDataContract.RecipeEntry.TABLE_NAME + "." + RecipeDataContract.RecipeEntry.COLUMN_ID +
            " WHERE " + RecipeDataContract.RecipeEntry.TABLE_NAME + "." +
            RecipeDataContract.RecipeEntry.COLUMN_ID + " = :id")
    Cursor selectByIdWithChildElements(long id);

    @Query("DELETE FROM " + RecipeDataContract.RecipeEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.RecipeEntry.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Update(onConflict = REPLACE)
    int update(Recipe recipe);
}
