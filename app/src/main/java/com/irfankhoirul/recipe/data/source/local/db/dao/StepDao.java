package com.irfankhoirul.recipe.data.source.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.irfankhoirul.recipe.data.pojo.Step;
import com.irfankhoirul.recipe.data.source.local.db.RecipeContract;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

@Dao
public interface StepDao {
    @Query("SELECT COUNT(*) FROM " + RecipeContract.StepEntry.TABLE_NAME)
    int count();

    @Insert(onConflict = REPLACE)
    long insert(Step step);

    @Insert(onConflict = REPLACE)
    long[] insertAll(Step[] steps);

    @Query("SELECT * FROM " + RecipeContract.StepEntry.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + RecipeContract.StepEntry.TABLE_NAME + " WHERE "
            + RecipeContract.StepEntry.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    @Query("DELETE FROM " + RecipeContract.StepEntry.TABLE_NAME + " WHERE "
            + RecipeContract.StepEntry.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Update(onConflict = REPLACE)
    int update(Step step);
}
