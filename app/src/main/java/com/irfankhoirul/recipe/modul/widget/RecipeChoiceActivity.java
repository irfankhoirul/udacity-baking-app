package com.irfankhoirul.recipe.modul.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RemoteViews;

import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.modul.recipe.RecipeActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class RecipeChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecipeChoiceDialog recipeChoiceDialog = new RecipeChoiceDialog();
        recipeChoiceDialog.setDialogListener(new RecipeChoiceDialog.RecipeChoiceDialogListener() {
            @Override
            public void onDismiss(Recipe recipe) {
//                RemoteViews views = new RemoteViews(getPackageName(), R.layout.recipe_widget);
//                views.setTextViewText(R.id.tv_recipe_name, recipe.getName());

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(RecipeChoiceActivity.this);
                RemoteViews remoteViews = new RemoteViews(RecipeChoiceActivity.this.getPackageName(), R.layout.recipe_widget);
                ComponentName recipeWidget = new ComponentName(RecipeChoiceActivity.this, RecipeWidgetProvider.class);

                final int[] appWidgetIds = appWidgetManager.getAppWidgetIds(recipeWidget);
                for (int appWidgetId : appWidgetIds) {
                    Log.v("RecipeWidgetService", "appWidgetId:" + appWidgetId);
                    Log.v("RecipeWidgetService", "intentAppWidgetId:" + getIntent().getIntExtra("widgetId", 0));
//                      appWidgetManager.updateAppWidget(appWidgetId, SmallWidgetProvider.buildRemoteView(application, appWidgetId, whatEverYouNeed));
                    if (appWidgetId == getIntent().getIntExtra("widgetId", 0)) {
                        remoteViews.setTextViewText(R.id.tv_recipe_name, recipe.getName());
                        remoteViews.setViewVisibility(R.id.ll_no_recipe, GONE);
                        remoteViews.setViewVisibility(R.id.ll_ingredient, VISIBLE);

                        // Set the GridWidgetService intent to act as the adapter for the GridView
                        Intent intent = new Intent(RecipeChoiceActivity.this, RecipeWidgetService.class);
//                intent.putExtra("recipeId", recipe.getId());
                        intent.setData(Uri.fromParts("recipeId", String.valueOf(recipe.getId()), null));
                        remoteViews.setRemoteAdapter(R.id.gv_ingredient, intent);
                        // Set the PlantDetailActivity intent to launch when clicked
                        Intent appIntent = new Intent(RecipeChoiceActivity.this, RecipeActivity.class);
                        appIntent.putExtra("recipeId", recipe.getId());
                        PendingIntent appPendingIntent = PendingIntent.getActivity(RecipeChoiceActivity.this, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        remoteViews.setPendingIntentTemplate(R.id.gv_ingredient, appPendingIntent);
                        // Handle empty gardens
                        remoteViews.setEmptyView(R.id.gv_ingredient, R.id.ll_no_recipe);

//                        appWidgetManager.updateAppWidget(recipeWidget, remoteViews);
                        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

                        break;
                    }
                }


                RecipeChoiceActivity.this.finish();
            }
        });
        recipeChoiceDialog.show(getSupportFragmentManager(), "recipeChoiceDialog");
    }
}
