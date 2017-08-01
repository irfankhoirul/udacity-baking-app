package com.irfankhoirul.recipe.data.pojo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.irfankhoirul.recipe.data.source.local.db.RecipeContract;

import java.util.List;

import static com.irfankhoirul.recipe.data.source.local.db.RecipeContract.RecipeEntry.COLUMN_DATE_ADDED;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

@Entity(tableName = RecipeContract.RecipeEntry.TABLE_NAME)
public class Recipe implements Parcelable {

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    @PrimaryKey
    @ColumnInfo(index = true, name = RecipeContract.RecipeEntry.COLUMN_ID)
    @SerializedName(RecipeContract.RecipeEntry.COLUMN_ID)
    @Expose
    private long id;
    @ColumnInfo(name = RecipeContract.RecipeEntry.COLUMN_NAME)
    @SerializedName(RecipeContract.RecipeEntry.COLUMN_NAME)
    @Expose
    private String name;
    @Relation(parentColumn = RecipeContract.RecipeEntry.COLUMN_ID,
            entityColumn = RecipeContract.IngredientEntry.COLUMN_RECIPE_ID)
    @SerializedName(RecipeContract.RecipeEntry.ENTITY_INGREDIENTS)
    @Expose
    private List<Ingredient> ingredients = null;
    @ColumnInfo(name = RecipeContract.RecipeEntry.ENTITY_STEPS)
    @SerializedName(RecipeContract.RecipeEntry.ENTITY_STEPS)
    @Expose
    private List<Step> steps = null;
    @ColumnInfo(name = RecipeContract.RecipeEntry.COLUMN_SERVINGS)
    @SerializedName(RecipeContract.RecipeEntry.COLUMN_SERVINGS)
    @Expose
    private int servings;
    @ColumnInfo(name = RecipeContract.RecipeEntry.COLUMN_IMAGE)
    @SerializedName(RecipeContract.RecipeEntry.COLUMN_IMAGE)
    @Expose
    private String image;
    /*
    * Addition, not exist in JSON
    * */
    @ColumnInfo(name = RecipeContract.RecipeEntry.COLUMN_FAVORITE)
    private boolean favorite;
    @ColumnInfo(name = COLUMN_DATE_ADDED)
    private long dateAdded;

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        id = in.readLong();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        servings = in.readInt();
        image = in.readString();
        favorite = in.readByte() != 0;
        dateAdded = in.readLong();
    }

    public static Recipe fromContentValues(ContentValues values) {
        final Recipe recipe = new Recipe();
        if (values.containsKey(RecipeContract.RecipeEntry.COLUMN_ID)) {
            recipe.id = values.getAsLong(RecipeContract.RecipeEntry.COLUMN_ID);
        }
        if (values.containsKey(RecipeContract.RecipeEntry.COLUMN_NAME)) {
            recipe.name = values.getAsString(RecipeContract.RecipeEntry.COLUMN_NAME);
        }
        if (values.containsKey(RecipeContract.RecipeEntry.ENTITY_INGREDIENTS)) {
            recipe.ingredients = new Gson().fromJson(values.getAsString(
                    RecipeContract.RecipeEntry.ENTITY_INGREDIENTS),
                    new TypeToken<List<Ingredient>>() {
                    }.getType());
        }
        if (values.containsKey(RecipeContract.RecipeEntry.ENTITY_STEPS)) {
            recipe.steps = new Gson().fromJson(values.getAsString(
                    RecipeContract.RecipeEntry.ENTITY_STEPS),
                    new TypeToken<List<Step>>() {
                    }.getType());
        }
        if (values.containsKey(RecipeContract.RecipeEntry.COLUMN_SERVINGS)) {
            recipe.servings = values.getAsInteger(RecipeContract.RecipeEntry.COLUMN_SERVINGS);
        }
        if (values.containsKey(RecipeContract.RecipeEntry.COLUMN_IMAGE)) {
            recipe.image = values.getAsString(RecipeContract.RecipeEntry.COLUMN_IMAGE);
        }
        if (values.containsKey(RecipeContract.RecipeEntry.COLUMN_FAVORITE)) {
            recipe.favorite = values.getAsBoolean(RecipeContract.RecipeEntry.COLUMN_FAVORITE);
        }
        if (values.containsKey(RecipeContract.RecipeEntry.COLUMN_DATE_ADDED)) {
            recipe.dateAdded = values.getAsLong(RecipeContract.RecipeEntry.COLUMN_DATE_ADDED);
        }

        return recipe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeLong(dateAdded);
    }
}
