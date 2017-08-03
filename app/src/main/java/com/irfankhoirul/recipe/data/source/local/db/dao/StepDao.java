package com.irfankhoirul.recipe.data.source.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.irfankhoirul.recipe.data.pojo.Step;
import com.irfankhoirul.recipe.data.source.local.db.RecipeDataContract;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

@Dao
public interface StepDao {
    @Query("SELECT COUNT(*) FROM " + RecipeDataContract.StepEntry.TABLE_NAME)
    int count();

    @Insert(onConflict = REPLACE)
    long insert(Step step);

    @Insert(onConflict = REPLACE)
    long[] insertAll(Step[] steps);

    @Query("SELECT * FROM " + RecipeDataContract.StepEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.StepEntry.COLUMN_RECIPE_ID + " = :recipeId")
    Cursor selectAll(long recipeId);

    @Query("SELECT * FROM " + RecipeDataContract.StepEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.StepEntry.COLUMN_ID + " = :stepId" + " AND "
            + RecipeDataContract.StepEntry.COLUMN_RECIPE_ID + " = :recipeId")
    Cursor selectById(long stepId, long recipeId);

    @Query("DELETE FROM " + RecipeDataContract.StepEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.StepEntry.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Update(onConflict = REPLACE)
    int update(Step step);
}
