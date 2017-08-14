package com.irfankhoirul.recipe.modul.recipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.util.SimpleIdlingResource;

import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.RecipeFragmentListener {

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Nullable
    public SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_recipe_container, RecipeFragment.newInstance(
                            getIntent().getLongExtra("recipeId", 0L)))
                    .commit();
        }
    }

    @Override
    public void onIdlingResourceStatusChanged(boolean isIdle) {
        if (mIdlingResource != null) {
            Log.d("SetIdlingResourceStatus", String.valueOf(isIdle));
            mIdlingResource.setIdleState(isIdle);
        }
    }
}
