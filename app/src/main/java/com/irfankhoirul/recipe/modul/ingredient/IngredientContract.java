package com.irfankhoirul.recipe.modul.ingredient;

import android.support.annotation.Nullable;

/**
 * Created by Irfan Khoirul on 8/3/2017.
 */

public interface IngredientContract {
    interface View {
        void setLoading(boolean status, @Nullable String message);

        void showError(String message);

        void updateIngredientList();

    }

    interface ViewModel {
        void loadIngredients();
    }
}
