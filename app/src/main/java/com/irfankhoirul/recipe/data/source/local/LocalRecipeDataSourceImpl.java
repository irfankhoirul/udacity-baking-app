package com.irfankhoirul.recipe.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.data.source.local.db.RecipeContract;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static android.R.attr.id;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

public class LocalRecipeDataSourceImpl implements LocalRecipeDataSource {

    private Context context;

    public LocalRecipeDataSourceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void getAll(LocalDataObserver<Cursor> cursorFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Cursor> e) throws Exception {
                // Get Recipe List
                Cursor cursor = context.getContentResolver()
                        .query(RecipeContract.RecipeEntry.RECIPE_CONTENT_URI,
                                null,
                                null,
                                null,
                                RecipeContract.RecipeEntry.COLUMN_DATE_ADDED + " DESC");

                if (cursor != null) {
                    Log.d("QueryCursor", String.valueOf(cursor.getCount()));
                    e.onNext(cursor);
                } else {
                    e.onError(new NullPointerException("Failed to query all data"));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cursorFavoriteDataObserver);
    }

    @Override
    public void getById(final long id, LocalDataObserver<Cursor> cursorFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Cursor> e) throws Exception {
                String stringId = Long.toString(id);
                Uri uri = RecipeContract.RecipeEntry.RECIPE_CONTENT_URI.buildUpon().appendPath(stringId).build();
                Cursor cursor = context.getContentResolver()
                        .query(uri,
                                null,
                                RecipeContract.RecipeEntry.COLUMN_ID + " = ?",
                                new String[]{String.valueOf(id)},
                                RecipeContract.RecipeEntry.COLUMN_DATE_ADDED + " DESC");

                if (cursor != null) {
                    Log.d("QueryCursor", String.valueOf(cursor.getCount()));
                    e.onNext(cursor);
                } else {
                    e.onError(new NullPointerException("Failed to query data with id: " + id));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cursorFavoriteDataObserver);
    }

    @Override
    public void insert(final Recipe recipe, LocalDataObserver<Uri> uriFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Uri> e) throws Exception {
                insertSingleRecipe(e, recipe);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uriFavoriteDataObserver);
    }

    private void insertSingleRecipe(@NonNull ObservableEmitter<Uri> e, Recipe recipe) {
        ContentValues contentValues = getContentValues(recipe);

        Uri uri = context.getContentResolver()
                .insert(RecipeContract.RecipeEntry.RECIPE_CONTENT_URI, contentValues);
        if (uri != null) {
            Log.d("InsertUri", uri.toString());
            e.onNext(uri);
        } else {
            e.onError(new NullPointerException("Failed to insert data"));
        }
    }

    @Override
    public void insertMany(final ArrayList<Recipe> recipes, LocalDataObserver<Uri> uriFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Uri> e) throws Exception {
                for (int i = 0; i < recipes.size(); i++) {
                    Recipe recipe = recipes.get(i);
                    insertSingleRecipe(e, recipe);
                    Log.v("InsertRecipe", String.valueOf(i));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uriFavoriteDataObserver);
    }

    @Override
    public void update(final Recipe recipe, LocalDataObserver<Integer> integerFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {

                String stringId = Long.toString(recipe.getId());
                Uri uri = RecipeContract.RecipeEntry.RECIPE_CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                ContentValues contentValues = getContentValues(recipe);

                int count = context.getContentResolver().update(uri, contentValues, null, null);

                if (count != 0) {
                    Log.d("UpdateCount", String.valueOf(count));
                    e.onNext(count);
                } else {
                    e.onError(new NullPointerException("Failed to update data with id: " + id));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integerFavoriteDataObserver);
    }

    @android.support.annotation.NonNull
    private ContentValues getContentValues(Recipe recipe) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_ID, recipe.getId());
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_NAME, recipe.getName());
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_SERVINGS, recipe.getServings());
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_IMAGE, recipe.getImage());
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_FAVORITE, recipe.isFavorite());
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_DATE_ADDED, recipe.getDateAdded());
        contentValues.put(RecipeContract.RecipeEntry.ENTITY_INGREDIENTS,
                new Gson().toJson(recipe.getIngredients()));
        contentValues.put(RecipeContract.RecipeEntry.ENTITY_STEPS,
                new Gson().toJson(recipe.getSteps()));
        return contentValues;
    }

    @Override
    public void delete(final long id, LocalDataObserver<Integer> integerFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {

                String stringId = Long.toString(id);
                Uri uri = RecipeContract.RecipeEntry.RECIPE_CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                int count = context.getContentResolver().delete(uri, null, null);

                if (count != 0) {
                    Log.d("DeleteCount", String.valueOf(count));
                    e.onNext(count);
                } else {
                    e.onError(new NullPointerException("Failed to delete data with id: " + id));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integerFavoriteDataObserver);
    }
}