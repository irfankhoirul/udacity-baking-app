package com.irfankhoirul.recipe.data.source.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;
import android.graphics.Movie;

import com.irfankhoirul.recipe.data.source.local.db.RecipeContract;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

@Dao
public interface RecipeDao {

    @Query("SELECT COUNT(*) FROM " + RecipeContract.RecipeEntry.TABLE_NAME)
    int count();

    @Insert(onConflict = REPLACE)
    long insert(Movie movie);

    @Insert(onConflict = REPLACE)
    long[] insertAll(Movie[] movies);

    @Query("SELECT * FROM " + RecipeContract.RecipeEntry.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + RecipeContract.RecipeEntry.TABLE_NAME + " WHERE "
            + RecipeContract.RecipeEntry.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    @Query("DELETE FROM " + RecipeContract.RecipeEntry.TABLE_NAME + " WHERE "
            + RecipeContract.RecipeEntry.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Update(onConflict = REPLACE)
    int update(Movie movie);
}
