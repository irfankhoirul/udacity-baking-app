package com.irfankhoirul.recipe.data.source.local.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

public class RecipeContract {

    public static final class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "recipe";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_FAVORITE = "favorite";
        public static final String COLUMN_DATE_ADDED = "dateAdded";
        public static final String ENTITY_INGREDIENTS = "ingredients";
        public static final String ENTITY_STEPS = "steps";

        public static final String AUTHORITY = "com.irfankhoirul.recipe";

        public static final int CODE_RECIPE_DIRECTORY = 100;
        public static final int CODE_RECIPE_ITEM = 101;

        public static final Uri BASE_CONTENT_URI =
                Uri.parse("content://" + AUTHORITY);

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(TABLE_NAME)
                        .build();
    }

    public static final class IngredientEntry implements BaseColumns {
        public static final String TABLE_NAME = "ingredient";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";
    }

    public static final class StepEntry implements BaseColumns {
        public static final String TABLE_NAME = "step";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO_URL = "videoURL";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnailURL";
    }

}
