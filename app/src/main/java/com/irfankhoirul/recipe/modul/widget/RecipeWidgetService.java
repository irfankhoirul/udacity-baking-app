package com.irfankhoirul.recipe.modul.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Ingredient;
import com.irfankhoirul.recipe.data.source.local.db.RecipeDataContract;

import java.util.ArrayList;


public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.v("RecipeWidgetService", "onGetViewFactory");
        return new RecipeRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private long recipeId;

    public RecipeRemoteViewsFactory(Context applicationContext, Intent intent) {
        Log.v("RecipeWidgetService", "Constructor");
        mContext = applicationContext;
        recipeId = Long.valueOf(intent.getData().getSchemeSpecificPart());
        Log.v("RecipeWidgetService", "RecipeId=" + recipeId);
    }

    @Override
    public void onCreate() {
        Log.v("RecipeWidgetService", "onCreate");
    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        Log.v("RecipeWidgetService", "onDataSetChanged");

        ingredients = getIngredients(recipeId);

        Log.v("RecipeWidgetService", "IngredientsLoaded");

    }

    private ArrayList<Ingredient> getIngredients(long recipeId) {
        Log.v("RecipeWidgetService", "StartLoadingContentProvider");
        Uri baseIngredientUri = RecipeDataContract
                .IngredientEntry
                .INGREDIENT_CONTENT_ITEM_URI.build();
        Log.v("RecipeWidgetService", "BaseURI:" + baseIngredientUri.toString());

        String ingredientUriString = baseIngredientUri.toString() + "/" + recipeId;
        Uri ingredientUri = Uri.parse(ingredientUriString);

        Log.v("RecipeWidgetService", "FinalURI:" + ingredientUri.toString());

        Cursor ingredientCursor = mContext.getContentResolver()
                .query(ingredientUri,
                        null,
                        null,
                        null,
                        RecipeDataContract.IngredientEntry.COLUMN_ID + " ASC");

        Log.v("RecipeWidgetService", "CursorLoaded");

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        if (ingredientCursor != null) {
            while (ingredientCursor.moveToNext()) {
                Ingredient ingredient = getIngredientFromCursor(ingredientCursor);
                Log.v("RecipeWidgetService", "Ingredient:" + ingredient.toString());
                ingredients.add(ingredient);
            }
            ingredientCursor.close();
            Log.v("RecipeWidgetService", "CursorClosed");
        }
        return ingredients;
    }

    private Ingredient getIngredientFromCursor(Cursor ingredientCursor) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientCursor.getLong(ingredientCursor
                .getColumnIndex(RecipeDataContract.IngredientEntry.COLUMN_ID)));
        ingredient.setRecipeId(ingredientCursor.getLong(ingredientCursor
                .getColumnIndex(RecipeDataContract.IngredientEntry.COLUMN_RECIPE_ID)));
        ingredient.setIngredient(ingredientCursor.getString(ingredientCursor
                .getColumnIndex(RecipeDataContract.IngredientEntry.COLUMN_INGREDIENT)));
        ingredient.setQuantity(ingredientCursor.getDouble(ingredientCursor
                .getColumnIndex(RecipeDataContract.IngredientEntry.COLUMN_QUANTITY)));
        ingredient.setMeasure(ingredientCursor.getString(ingredientCursor
                .getColumnIndex(RecipeDataContract.IngredientEntry.COLUMN_MEASURE)));
        return ingredient;
    }


    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        Log.v("RecipeWidgetService", "getViewAt");

        if (ingredients.size() == 0) {
            return null;
        }

        Ingredient ingredient = ingredients.get(position);
        Log.v("RecipeWidgetService", ingredient.toString());

        String ingredientName = ingredient.getIngredient();
        double quantity = ingredient.getQuantity();
        String measure = ingredient.getMeasure();

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_ingredient);
        views.setTextViewText(R.id.tv_ingredient, ingredientName);
        views.setTextViewText(R.id.tv_quantity, quantity + " " + measure);

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        Bundle extras = new Bundle();
        extras.putLong("recipeId", recipeId);
        extras.putLong("ingredientId", ingredients.get(position).getId());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.ll_ingredient, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

