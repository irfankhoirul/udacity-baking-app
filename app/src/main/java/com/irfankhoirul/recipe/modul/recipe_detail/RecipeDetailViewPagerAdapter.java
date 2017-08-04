package com.irfankhoirul.recipe.modul.recipe_detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.irfankhoirul.recipe.modul.ingredient.IngredientFragment;
import com.irfankhoirul.recipe.modul.step.StepFragment;

/**
 * Created by Irfan Khoirul on 8/3/2017.
 */

public class RecipeDetailViewPagerAdapter extends FragmentPagerAdapter {

    private IngredientFragment ingredientFragment;
    private StepFragment stepFragment;

    public RecipeDetailViewPagerAdapter(FragmentManager fm,
                                        IngredientFragment ingredientFragment,
                                        StepFragment stepFragment) {
        super(fm);
        this.ingredientFragment = ingredientFragment;
        this.stepFragment = stepFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ingredientFragment;
            case 1:
                return stepFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Ingredients";
            case 1:
                return "Steps";
        }
        return null;
    }
}
